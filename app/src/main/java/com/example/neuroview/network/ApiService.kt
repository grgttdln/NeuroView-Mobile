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
data class ApiResponse(
    val success: Boolean,
    val message: String? = null,
    val data: ImageData? = null
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
            val inputStream = context.contentResolver.openInputStream(imageUri)
            val imageBytes = inputStream?.use { input ->
                val outputStream = ByteArrayOutputStream()
                input.copyTo(outputStream)
                outputStream.toByteArray()
            } ?: throw Exception("Could not read image file")

            val fileName = imageName.ifBlank { "image.jpg" }
            val mimeType = context.contentResolver.getType(imageUri) ?: "image/jpeg"

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

            when (response.status) {
                HttpStatusCode.OK, HttpStatusCode.Created -> {
                    val responseText = response.bodyAsText()
                    val jsonResponse = Json { ignoreUnknownKeys = true }.decodeFromString<ApiResponse>(responseText)
                    Result.success(jsonResponse)
                }
                else -> {
                    Result.failure(Exception("Server returned ${response.status.value}: ${response.bodyAsText()}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}