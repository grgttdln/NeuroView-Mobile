package com.example.neuroview.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.neuroview.Routes
import com.example.neuroview.components.BottomNavigationBar
import com.example.neuroview.components.TopAppBar

@Composable
fun ResultScreen(navController: NavController) {
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
                currentRoute = Routes.RESULT,
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "RESULT",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF737373)
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Placeholder Result",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(24.dp))

                // two image blocks side by side

                Text(
                    text = "Confidence Score",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF737373)
                )
                Spacer(modifier = Modifier.height(16.dp))

                // bar graph

                Text(
                    text = "Insights",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF737373)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Sample Insights",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResultScreenPreview() {
    // Mock NavController for preview
    val mockNavController = rememberNavController()
    
    MaterialTheme {
        ResultScreen(navController = mockNavController)
    }
}