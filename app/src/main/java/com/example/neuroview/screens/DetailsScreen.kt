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
fun DetailsScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Details",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        Text(
            text = "This is the Details screen where you can view detailed information and analysis results.",
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        Button(
            onClick = { navController.navigate(Routes.HOME) }
        ) {
            Text("Back to Home")
        }
    }
} 