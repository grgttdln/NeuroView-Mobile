package com.example.neuroview.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.neuroview.components.BottomNavigationBar
import com.example.neuroview.components.TopAppBar
import com.example.neuroview.network.PredictionData
import kotlinx.serialization.json.Json
import java.net.URLDecoder

@Composable
fun ResultScreen(
    predictionJson: String? = null,
    imageUri: String? = null,
    paddingValues: PaddingValues,
    onNavigateToHome: () -> Unit,
    onNavigateToDashboard: () -> Unit,
    onNavigateToUpload: () -> Unit,
    onNavigateToPastRecords: () -> Unit
) {

    val predictionData = remember(predictionJson) {
        predictionJson?.let { json ->
            try {
                println("NeuroView: Raw prediction JSON: $json")
                
                // Try different decoding approaches
                val decodedJson = when {
                    json.startsWith("{") -> {
                        // Already decoded JSON
                        println("NeuroView: JSON appears to be already decoded")
                        json
                    }
                    else -> {
                        // URL encoded JSON, decode it
                        println("NeuroView: Decoding URL encoded JSON")
                        URLDecoder.decode(json, "UTF-8")
                    }
                }
                
                println("NeuroView: Decoded JSON: $decodedJson")
                
                val jsonParser = Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    coerceInputValues = true
                }
                jsonParser.decodeFromString<PredictionData>(decodedJson)
            } catch (e: Exception) {
                println("NeuroView: Error parsing prediction data: ${e.message}")
                e.printStackTrace()
                null
            }
        }
    }

    // Process the image URI
    val decodedImageUri = remember(imageUri) {
        imageUri?.let { uri ->
            try {
                val decodedUri = URLDecoder.decode(uri, "UTF-8")
                println("NeuroView: Decoded image URI: $decodedUri")
                Uri.parse(decodedUri)
            } catch (e: Exception) {
                println("NeuroView: Failed to decode image URI: ${e.message}")
                null
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.Black,
                textColor = Color.White,
                logoSize = 42,
                titleFontSize = 28
            )
        },
        bottomBar = {
            BottomNavigationBar(
                currentRoute = "result",
                onNavigateToHome = onNavigateToHome,
                onNavigateToDashboard = onNavigateToDashboard,
                onNavigateToUpload = onNavigateToUpload,
                onNavigateToPastRecords = onNavigateToPastRecords,
                bottomPadding = 18,
                navBarHeight = 80,
                regularIconSize = 32,
                fabIconSize = 32,
                fabSize = 64,
                navBarWidth = 0.8f,
                horizontalPadding = 16
            )
        }
    ) { innerPaddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(paddingValues) // Apply outer padding from Activity
                .padding(innerPaddingValues) // Apply inner padding from Scaffold
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "RESULT",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF737373),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(2.dp))
            
            if (predictionData != null && predictionData.success) {
                PredictionResultContent(
                    predictionData = predictionData,
                    imageUri = decodedImageUri
                )
            } else {
                NoResultContent(
                    predictionData = predictionData,
                    rawJson = predictionJson,
                    hasRawJson = predictionJson != null,
                    imageUri = decodedImageUri,
                    debugInfo = mapOf(
                        "PredictionJson" to predictionJson
                    )
                )
            }
        }
    }
}

@Composable
fun PredictionResultContent(
    predictionData: PredictionData,
    imageUri: Uri? = null
) {
    val tumorType = predictionData.tumor_type ?: "Unknown"
    val confidence = predictionData.confidence ?: 0.0
    val classProbs = predictionData.class_probabilities ?: emptyMap()

    // Main prediction result card
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Black)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = tumorType.replaceFirstChar { it.uppercase() },
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    imageUri?.let { uri ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Black),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "INPUT IMAGE",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF737373),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Image(
                    painter = rememberAsyncImagePainter(model = uri),
                    contentDescription = "Uploaded brain scan",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    // Class probabilities
    if (classProbs.isNotEmpty()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "CONFIDENCE SCORES",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF737373),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            classProbs.toList().sortedByDescending { it.second }
                .forEachIndexed { index, (className, probability) ->
                    ProbabilityBar(
                        className = className,
                        probability = probability
                    )
                    if (index < classProbs.size - 1) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
        }
    }

    Spacer(modifier = Modifier.height(32.dp))

    // Insights section
    InsightsSection(tumorType = tumorType, confidence = confidence)

    Spacer(modifier = Modifier.height(32.dp))
}


@Composable
fun ProbabilityBar(className: String, probability: Double) {
    Column(modifier = Modifier.fillMaxWidth()) {


        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = className.uppercase(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                modifier = Modifier.width(100.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(20.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF2A2A2A))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(probability.toFloat())
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "${(probability * 100).toInt()}%",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF737373),
                modifier = Modifier.width(40.dp)
            )
        }
    }
}


@Composable
fun InsightsSection(tumorType: String, confidence: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Black),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "INSIGHTS",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF737373),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            val insights = getInsightsForTumorType(tumorType, confidence)
            insights.forEach { insight ->
                Text(
                    text = insight,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}


@Composable
fun NoResultContent(
    predictionData: PredictionData? = null,
    rawJson: String? = null,
    hasRawJson: Boolean = false,
    imageUri: Uri? = null,
    debugInfo: Map<String, String?> = emptyMap()
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "No Analysis Available",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Show uploaded image even if analysis failed
        imageUri?.let { uri ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Your Uploaded Image",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF737373),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Image(
                        painter = rememberAsyncImagePainter(model = uri),
                        contentDescription = "Uploaded brain scan",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Show different messages based on what data we have
        when {
            !hasRawJson -> {
                Text(
                    text = "No prediction data was received.\n\nThis means the navigation parameter was not passed correctly from the upload screen.\n\nCheck the debug info below to see what data was attempted to be passed.",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF737373),
                    textAlign = TextAlign.Center
                )
            }
            predictionData == null && hasRawJson -> {
                Text(
                    text = "Prediction data was received but failed to parse.\n\nThe JSON format may be corrupted or invalid.",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFFFF9800),
                    textAlign = TextAlign.Center
                )
            }
            predictionData != null && !predictionData.success -> {
                Text(
                    text = "Analysis failed on the backend: ${predictionData.error ?: predictionData.message ?: "Unknown error"}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFFFF5722),
                    textAlign = TextAlign.Center
                )
            }
            else -> {
                Text(
                    text = "Unable to analyze the uploaded image.\n\nPlease try uploading a clear brain scan image.",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF737373),
                    textAlign = TextAlign.Center
                )
            }
        }

            Spacer(modifier = Modifier.height(16.dp))

        // Debug info card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "Debug Information:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF737373)
                )
                Spacer(modifier = Modifier.height(4.dp))

                debugInfo.forEach { (source, value) ->
                    val hasValue = !value.isNullOrBlank()
                    Text(
                        text = "$source: ${if (hasValue) "✓ (${value?.take(20)}...)" else "✗ No data"}",
                        fontSize = 10.sp,
                        color = if (hasValue) Color(0xFF4CAF50) else Color(0xFFFF5722)
                    )
                }

                if (hasRawJson) {
                    Text(
                        text = "JSON Length: ${rawJson?.length ?: 0}",
                        fontSize = 10.sp,
                        color = Color(0xFF737373)
                    )
                }

                predictionData?.let { data ->
                    Text(
                        text = "Parsed Success: ${data.success}",
                        fontSize = 10.sp,
                        color = if (data.success) Color(0xFF4CAF50) else Color(0xFFFF5722)
                    )
                    data.error?.let {
                        Text(
                            text = "Error: $it",
                            fontSize = 10.sp,
                            color = Color(0xFFFF5722)
                        )
                    }
                }
            }
        }
    }
}



fun getInsightsForTumorType(tumorType: String, confidence: Double): List<String> {
    val baseInsights = when (tumorType.lowercase()) {
        "glioma" -> listOf(
            "Gliomas are tumors that arise from glial cells in the brain",
            "They account for about 80% of malignant brain tumors",
            "Treatment typically involves surgery, radiation, and chemotherapy"
        )
        "meningioma" -> listOf(
            "Meningiomas arise from the meninges that cover the brain",
            "Most meningiomas are benign and grow slowly",
            "Treatment options include observation, surgery, or radiation therapy"
        )
        "pituitary" -> listOf(
            "Pituitary tumors occur in the pituitary gland",
            "Most are benign and may affect hormone production",
            "Treatment may include medication, surgery, or radiation"
        )
        "notumor" -> listOf(
            "No tumor detected in the brain scan",
            "The brain tissue appears normal",
            "Regular monitoring may still be recommended"
        )
        else -> listOf("Unable to provide specific insights for this classification")
    }

    val confidenceInsight = when {
        confidence >= 0.9 -> "Very high confidence in this prediction"
        confidence >= 0.8 -> "High confidence in this prediction"
        confidence >= 0.7 -> "Moderate confidence in this prediction"
        confidence >= 0.6 -> "Fair confidence in this prediction"
        else -> "Low confidence - further analysis recommended"
    }

    return baseInsights + confidenceInsight
}

@Preview(showBackground = true)
@Composable
fun ResultScreenPreview() {
    MaterialTheme {
        ResultScreen(
            predictionJson = null,
            imageUri = null,
            paddingValues = PaddingValues(0.dp),
            onNavigateToHome = {},
            onNavigateToDashboard = {},
            onNavigateToUpload = {},
            onNavigateToPastRecords = {}
        )
    }
}