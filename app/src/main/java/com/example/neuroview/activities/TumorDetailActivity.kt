package com.example.neuroview.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.neuroview.screens.TumorDetailScreen
import com.example.neuroview.ui.theme.NeuroViewTheme

class TumorDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val tumorName = intent.getStringExtra("tumorName")

        setContent {
            NeuroViewTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
                    TumorDetailScreen(
                        tumorName = tumorName,
                        paddingValues = paddingValues,
                        onNavigateToHome = {
                            startActivity(Intent(this@TumorDetailActivity, HomeActivity::class.java))
                        },
                        onNavigateToDashboard = {
                            startActivity(Intent(this@TumorDetailActivity, DashboardActivity::class.java))
                        },
                        onNavigateToUpload = {
                            startActivity(Intent(this@TumorDetailActivity, UploadImageActivity::class.java))
                        },
                        onNavigateToPastRecords = {
                            startActivity(Intent(this@TumorDetailActivity, PastRecordsActivity::class.java))
                        }
                    )
                }
            }
        }
    }
}
