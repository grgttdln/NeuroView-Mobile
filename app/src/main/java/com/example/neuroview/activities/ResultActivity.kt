package com.example.neuroview.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.neuroview.screens.ResultScreen
import com.example.neuroview.ui.theme.NeuroViewTheme

class ResultActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val predictionJson = intent.getStringExtra("predictionJson")
        val imageUri = intent.getStringExtra("imageUri")

        setContent {
            NeuroViewTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
                    ResultScreen(
                        predictionJson = predictionJson,
                        imageUri = imageUri,
                        paddingValues = paddingValues,
                        onNavigateToHome = {
                            startActivity(Intent(this@ResultActivity, HomeActivity::class.java))
                        },
                        onNavigateToDashboard = {
                            startActivity(Intent(this@ResultActivity, DashboardActivity::class.java))
                        },
                        onNavigateToUpload = {
                            startActivity(Intent(this@ResultActivity, UploadImageActivity::class.java))
                        },
                        onNavigateToPastRecords = {
                            startActivity(Intent(this@ResultActivity, PastRecordsActivity::class.java))
                        }
                    )
                }
            }
        }
    }
}
