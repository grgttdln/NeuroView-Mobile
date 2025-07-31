package com.example.neuroview.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.neuroview.screens.DashboardScreen
import com.example.neuroview.ui.theme.NeuroViewTheme

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NeuroViewTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
                    DashboardScreen(
                        paddingValues = paddingValues,
                        onNavigateToHome = {
                            startActivity(Intent(this@DashboardActivity, HomeActivity::class.java))
                        },
                        onNavigateToUpload = {
                            startActivity(Intent(this@DashboardActivity, UploadImageActivity::class.java))
                        },
                        onNavigateToPastRecords = {
                            startActivity(Intent(this@DashboardActivity, PastRecordsActivity::class.java))
                        },
                        onNavigateToTumorDetail = { tumorName ->
                            val intent = Intent(this@DashboardActivity, TumorDetailActivity::class.java)
                            intent.putExtra("tumorName", tumorName)
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}
