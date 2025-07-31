package com.example.neuroview.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer // Import for alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.neuroview.R // Assuming your resources are in the 'R' class
import com.example.neuroview.data.TumorData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TumorDetailScreen(
    tumorName: String?,
    paddingValues: PaddingValues,
    onNavigateToHome: () -> Unit,
    onNavigateToDashboard: () -> Unit,
    onNavigateToUpload: () -> Unit,
    onNavigateToPastRecords: () -> Unit
) {
    val tumor = tumorName?.let { TumorData.getTumorByName(it) }

    if (tumor == null) {
        // Handle case where tumor is not found
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(paddingValues), // Apply padding values here
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Tumor information not found",
                    color = Color.White,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { onNavigateToDashboard() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text("Back to Dashboard", color = Color.Black)
                }
            }
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "LEARN",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF737373),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onNavigateToDashboard() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF737373)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Do nothing, this is just for spacing */ }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.Transparent,
                            modifier = Modifier.graphicsLayer(alpha = 0f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black
                )
            )
        }
    ) { innerPaddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(paddingValues) // Apply outer padding from Activity
                .padding(innerPaddingValues) // Apply inner padding from Scaffold
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 14.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp)) // Spacer to push content down from top bar

            // Glioma Title (centered and larger)
            Text(
                text = tumor.name,
                fontSize = 32.sp, // Increased font size for title
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Tumor Image
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0B0B0B))
            ) {
                Image(
                    painter = painterResource(id = tumor.imageResource), // Make sure tumor.imageResource is correctly set to your drawable resource
                    contentDescription = "${tumor.name} image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp))
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Short Description (the main description as in the image)
            Text(
                text = tumor.detailedDescription, // This should contain the "Gliomas are a type of tumor..." text
                fontSize = 16.sp, // Adjusted font size
                fontWeight = FontWeight.Normal, // Adjusted font weight
                color = Color(0xFF737373),
                textAlign = TextAlign.Center, // Centered the text
                lineHeight = 24.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Common Symptoms Section
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally // Center the content within this column
            ) {
                // Icon for Common Symptoms (Placeholder, replace with actual icon resource if available)
                Icon(
                    painter = painterResource(id = R.drawable.ic_symptoms), // Replace with your actual symptom icon
                    contentDescription = "Symptoms Icon",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Common Symptoms",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = tumor.symptoms.joinToString(separator = ", "), // Join symptoms with a comma
                    fontSize = 16.sp,
                    color = Color(0xFF737373),
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Treatment Section
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally // Center the content within this column
            ) {
                // Icon for Treatment (Placeholder, replace with actual icon resource if available)
                Icon(
                    painter = painterResource(id = R.drawable.ic_treatment), // Replace with your actual treatment icon
                    contentDescription = "Treatment Icon",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Treatment",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = tumor.treatment,
                    fontSize = 16.sp,
                    color = Color(0xFF737373),
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}