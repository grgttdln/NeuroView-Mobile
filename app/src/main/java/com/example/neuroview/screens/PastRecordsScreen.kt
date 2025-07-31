package com.example.neuroview.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.neuroview.Routes
import com.example.neuroview.components.BottomNavigationBar
import com.example.neuroview.components.TopAppBar
import com.example.neuroview.network.ApiService
import com.example.neuroview.network.ImageData
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.text.style.TextOverflow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun PastRecordsScreen(navController: NavController) {
    val apiService = remember { ApiService() }
    var pastRecords by remember { mutableStateOf<List<ImageData>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var selectedFilter by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        launch {
            val result = apiService.getPastRecords()
            isLoading = false
            result.onSuccess { response ->
                pastRecords = response.data
                errorMessage = null
            }.onFailure { e ->
                errorMessage = "Failed to fetch records: ${e.message}"
                pastRecords = emptyList()
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
                navController = navController,
                currentRoute = Routes.PAST_RECORDS,
                bottomPadding = 18,
                navBarHeight = 80,
                regularIconSize = 32,
                fabIconSize = 32,
                fabSize = 64,
                navBarWidth = 0.8f,
                horizontalPadding = 16
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "COLLECTIONS",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF737373),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                textAlign = TextAlign.Center
            )

            Text(
                text = "Repository of NeuroView",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilterChip(
                    text = "Glioma",
                    isSelected = selectedFilter == "glioma"
                ) {
                    selectedFilter = if (selectedFilter == "glioma") null else "glioma"
                }
                FilterChip(
                    text = "No Tumor",
                    isSelected = selectedFilter == "notumor"
                ) {
                    selectedFilter = if (selectedFilter == "notumor") null else "notumor"
                }
                FilterChip(
                    text = "Pituitary",
                    isSelected = selectedFilter == "pituitary"
                ) {
                    selectedFilter = if (selectedFilter == "pituitary") null else "pituitary"
                }
                FilterChip(
                    text = "Meningioma",
                    isSelected = selectedFilter == "meningioma"
                ) {
                    selectedFilter = if (selectedFilter == "meningioma") null else "meningioma"
                }
            }

            if (isLoading) {
                CircularProgressIndicator(color = Color.White)
                Text("Loading records...", color = Color.White, modifier = Modifier.padding(top = 16.dp))
            } else if (errorMessage != null) {
                Text(
                    text = errorMessage ?: "An unknown error occurred.",
                    color = Color.Red,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Button(onClick = {
                    isLoading = true
                    errorMessage = null
                    coroutineScope.launch {
                        val result = apiService.getPastRecords()
                        isLoading = false
                        result.onSuccess { response ->
                            pastRecords = response.data
                            errorMessage = null
                        }.onFailure { e ->
                            errorMessage = "Failed to fetch records: ${e.message}"
                            pastRecords = emptyList()
                        }
                    }
                }) {
                    Text("Retry")
                }
            } else {
                val filteredRecords = remember(pastRecords, selectedFilter) {
                    if (selectedFilter == null) {
                        pastRecords
                    } else {
                        pastRecords.filter { it.information?.tumor_type?.lowercase(Locale.getDefault()) == selectedFilter?.lowercase(Locale.getDefault()) }
                    }
                }

                if (filteredRecords.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "No records found for the selected filter.",
                            color = Color.White,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                        if (selectedFilter != null) {
                            Button(onClick = { selectedFilter = null }) {
                                Text("Show All Records")
                            }
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Re-introducing the chunked(2) logic for 2 columns
                        val chunkedRecords = filteredRecords.chunked(2)
                        items(chunkedRecords) { rowItems ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround // Space out items in the row
                            ) {
                                rowItems.forEach { record ->
                                    // Each card takes half the width, with padding in between
                                    // We use `weight(1f)` and `fillMaxHeight()` to make them share space and maintain height
                                    Box(modifier = Modifier.weight(1f).padding(horizontal = 8.dp)) {
                                        RecordCard(
                                            record = record,
                                            onCardClick = { clickedRecord ->
                                                // Convert ImageData to PredictionData format for ResultScreen
                                                clickedRecord.information?.let { predictionInfo ->
                                                    try {
                                                        val json = Json {
                                                            ignoreUnknownKeys = true
                                                            isLenient = true
                                                        }
                                                        val predictionJson = json.encodeToString(predictionInfo)
                                                        val encodedPredictionJson = URLEncoder.encode(predictionJson, StandardCharsets.UTF_8.toString())
                                                        val encodedImageUri = URLEncoder.encode(clickedRecord.url, StandardCharsets.UTF_8.toString())

                                                        navController.navigate("result?predictionJson=$encodedPredictionJson&imageUri=$encodedImageUri")
                                                    } catch (e: Exception) {
                                                        println("NeuroView: Failed to encode data for navigation: ${e.message}")
                                                    }
                                                }
                                            }
                                        )
                                    }
                                }
                                // If there's only one item in the last row, add an empty box to balance
                                if (rowItems.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f).padding(horizontal = 8.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilterChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) Color.White else Color.DarkGray,
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .clickable(onClick = onClick)
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.Black else Color.White,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun RecordCard(record: ImageData, onCardClick: (ImageData) -> Unit) {
    // Keep fixed sizes for the card and image as per the previous successful iteration
    val cardSize = 180.dp
    val imageHeight = 100.dp

    Card(
        modifier = Modifier
            .width(cardSize) // Fixed width for each card
            .height(cardSize) // Fixed height for each card, making it square
            .clickable { onCardClick(record) }, // Make entire card clickable
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1E)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Image Box with fixed height
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = record.url),
                    contentDescription = "Brain Scan Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.Center
                )
            }

            // Text elements
            Text(
                text = record.information?.tumor_type?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } ?: "N/A",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = record.information?.confidence?.let { "%.2f".format(it * 100) + "%" } ?: "N/A",
                color = Color.LightGray,
                fontSize = 12.sp
            )

            // Date and View > in a Row
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX", Locale.getDefault())
            val outputFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
            val date = try {
                record.uploaded_at?.let { inputFormat.parse(it) }
            } catch (_: Exception) {
                null
            }
            val formattedDate = date?.let { outputFormat.format(it) } ?: "N/A"

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formattedDate,
                    color = Color.Gray,
                    fontSize = 10.sp
                )
                Text(
                    text = "View >",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}