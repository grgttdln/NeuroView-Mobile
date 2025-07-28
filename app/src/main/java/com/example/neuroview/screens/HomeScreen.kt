package com.example.neuroview.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.neuroview.Routes

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Home",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        Text(
            text = "Welcome to NeuroView",
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        // Navigation buttons to other screens
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { navController.navigate(Routes.DASHBOARD) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Go to Dashboard")
            }
            
            Button(
                onClick = { navController.navigate(Routes.UPLOAD_IMAGE) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Upload Image")
            }
            
            Button(
                onClick = { navController.navigate(Routes.DETAILS) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View Details")
            }
            
            Button(
                onClick = { navController.navigate(Routes.PAST_RECORDS) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Past Records")
            }
        }
    }
} 