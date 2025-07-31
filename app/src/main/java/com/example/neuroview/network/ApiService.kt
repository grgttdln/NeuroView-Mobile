package com.example.neuroview.network

import android.content.Context
import android.net.Uri
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.ByteArrayOutputStream

@Serializable
data class CreateImageRequest(
    val name: String
)

@Serializable
data class PredictionData(
    val success: Boolean,
    val predicted_class: Int? = null,
    val tumor_type: String? = null,
    val confidence: Double? = null,
    val probabilities: List<Double>? = null,
    val class_probabilities: Map<String, Double>? = null,
    val message: String? = null,
    val error: String? = null
)

@Serializable
data class ApiResponse(
    val success: Boolean,
    val message: String? = null,
    val data: ImageData? = null,
    val prediction: PredictionData? = null,
    val prediction_error: String? = null
)

@Serializable
data class ImageData(
    val id: String,
    val name: String,
    val url: String,
    val uploaded_at: String
)

class ApiService {
    private val baseUrl = "http://192.168.1.4:5001/api/auto"

    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    // Helper function to test API connectivity
    suspend fun testConnection(): Result<String> {
        return try {
            val response: HttpResponse = client.get(baseUrl.replace("/api/auto", "/health"))
            when (response.status) {
                HttpStatusCode.OK -> Result.success("Connected successfully")
                else -> Result.failure(Exception("Server not responding: ${response.status.value}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Cannot connect to server: ${e.message}"))
        }
    }

    suspend fun createImage(name: String): Result<ApiResponse> {
        return try {
            val response: HttpResponse = client.post(baseUrl) {
                contentType(ContentType.Application.Json)
                setBody(CreateImageRequest(name = name))
            }

            when (response.status) {
                HttpStatusCode.OK, HttpStatusCode.Created -> {
                    Result.success(ApiResponse(success = true, message = "Image created successfully"))
                }
                else -> {
                    Result.failure(Exception("Server returned ${response.status.value}: ${response.bodyAsText()}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun uploadImage(context: Context, imageUri: Uri, imageName: String): Result<ApiResponse> {
        return try {
            println("NeuroView: Starting image upload to $baseUrl")
            
            val inputStream = context.contentResolver.openInputStream(imageUri)
            val imageBytes = inputStream?.use { input ->
                val outputStream = ByteArrayOutputStream()
                input.copyTo(outputStream)
                outputStream.toByteArray()
            } ?: throw Exception("Could not read image file")

            println("NeuroView: Image size: ${imageBytes.size} bytes")

            val fileName = imageName.ifBlank { "image.jpg" }
            val mimeType = context.contentResolver.getType(imageUri) ?: "image/jpeg"
            
            println("NeuroView: Uploading file: $fileName, type: $mimeType")

            val response: HttpResponse = client.submitFormWithBinaryData(
                url = baseUrl,
                formData = formData {
                    append("file", imageBytes, Headers.build {
                        append(HttpHeaders.ContentType, mimeType)
                        append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                    })
                    append("name", imageName)
                }
            )

            println("NeuroView: Response status: ${response.status}")

            when (response.status) {
                HttpStatusCode.OK, HttpStatusCode.Created -> {
                    val responseText = response.bodyAsText()
                    println("NeuroView: Response body: $responseText")
                    
                    val jsonResponse = Json { ignoreUnknownKeys = true }.decodeFromString<ApiResponse>(responseText)
                    println("NeuroView: Parsed response: $jsonResponse")
                    
                    if (jsonResponse.prediction == null) {
                        println("NeuroView: Warning - No prediction data in response")
                    } else {
                        println("NeuroView: Prediction success: ${jsonResponse.prediction?.success}")
                    }
                    
                    Result.success(jsonResponse)
                }
                else -> {
                    val errorBody = response.bodyAsText()
                    println("NeuroView: Error response: $errorBody")
                    Result.failure(Exception("Server returned ${response.status.value}: $errorBody"))
                }
            }
        } catch (e: Exception) {
            println("NeuroView: Upload exception: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }

    fun close() {
        client.close()
    }
}