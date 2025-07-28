package com.example.neuroview.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.neuroview.Routes
import com.example.neuroview.components.BottomNavigationBar
import com.example.neuroview.components.TopAppBar

@Composable
fun UploadImageScreen(navController: NavController) {
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
                currentRoute = Routes.UPLOAD_IMAGE,
                bottomPadding = 18, // Adjust this value to move navbar up/down
                navBarHeight = 80,   // Adjust this to change navbar height
                regularIconSize = 32, // Size for regular nav icons
                fabIconSize = 32,     // Size for the elevated FAB icon
                fabSize = 64,          // Size of the entire FAB button
                navBarWidth= 0.8f,      // Controls the width as a fraction (0.8f = 80% width)
                horizontalPadding = 16   // Controls the horizontal margins
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(paddingValues)
                .padding(16.dp),

            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Upload Image",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Text(
                text = "This is the Upload Image screen where you can upload images for analysis.",
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Button(
                onClick = { navController.navigate(Routes.HOME) }
            ) {
                Text("Back to Home")
            }
        }
    }
}
