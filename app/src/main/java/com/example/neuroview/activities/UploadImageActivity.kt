package com.example.neuroview.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.neuroview.screens.UploadImageScreen
import com.example.neuroview.ui.theme.NeuroViewTheme

class UploadImageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NeuroViewTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
                    UploadImageScreen(
                        paddingValues = paddingValues,
                        onNavigateToHome = {
                            startActivity(Intent(this@UploadImageActivity, HomeActivity::class.java))
                        },
                        onNavigateToDashboard = {
                            startActivity(Intent(this@UploadImageActivity, DashboardActivity::class.java))
                        },
                        onNavigateToPastRecords = {
                            startActivity(Intent(this@UploadImageActivity, PastRecordsActivity::class.java))
                        },
                        onNavigateToResult = { predictionJson, imageUri ->
                            val intent = Intent(this@UploadImageActivity, ResultActivity::class.java)
                            intent.putExtra("predictionJson", predictionJson)
                            intent.putExtra("imageUri", imageUri)
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}
