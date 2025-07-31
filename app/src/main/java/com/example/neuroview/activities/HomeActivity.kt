package com.example.neuroview.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.neuroview.screens.HomeScreen
import com.example.neuroview.ui.theme.NeuroViewTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NeuroViewTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
                    HomeScreen(
                        paddingValues = paddingValues,
                        onNavigateToDashboard = {
                            startActivity(Intent(this@HomeActivity, DashboardActivity::class.java))
                        },
                        onNavigateToUpload = {
                            startActivity(Intent(this@HomeActivity, UploadImageActivity::class.java))
                        },
                        onNavigateToPastRecords = {
                            startActivity(Intent(this@HomeActivity, PastRecordsActivity::class.java))
                        }
                    )
                }
            }
        }
    }
}
