package com.example.neuroview.screens

import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.neuroview.R
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.neuroview.ui.theme.NeuroViewTheme

@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    onNavigateToDashboard: () -> Unit,
    onNavigateToUpload: () -> Unit,
    onNavigateToPastRecords: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(id = R.drawable.home),
                contentScale = ContentScale.Crop
            )
            .padding(paddingValues) // Apply the padding values here
            .padding(24.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.home_logo),
            contentDescription = "Home Logo",
            modifier = Modifier
                .size(360.dp)
                .align(Alignment.Center)
                .offset(y = (-60).dp)
        )

        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            Text(
                text = "Welcome to",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "NeuroView",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "Revolutionizing brain tumor diagnosis with advanced AI technology for faster, more accurate results.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Button(
                onClick = { onNavigateToDashboard() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Get Started",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Home Screen Preview")
@Composable
fun HomeScreenPreview() {
    NeuroViewTheme {
        HomeScreen(
            paddingValues = PaddingValues(0.dp),
            onNavigateToDashboard = {},
            onNavigateToUpload = {},
            onNavigateToPastRecords = {}
        )
    }
}
