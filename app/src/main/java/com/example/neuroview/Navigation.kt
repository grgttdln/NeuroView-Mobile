package com.example.neuroview

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.neuroview.screens.DashboardScreen
import com.example.neuroview.screens.DetailsScreen
import com.example.neuroview.screens.HomeScreen
import com.example.neuroview.screens.PastRecordsScreen
import com.example.neuroview.screens.UploadImageScreen

object Routes {
    const val HOME = "home"
    const val DASHBOARD = "dashboard"
    const val UPLOAD_IMAGE = "upload_image"
    const val DETAILS = "details"
    const val PAST_RECORDS = "past_records"
}

@Composable
fun NeuroViewNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            HomeScreen(navController = navController)
        }
        composable(Routes.DASHBOARD) {
            DashboardScreen(navController = navController)
        }
        composable(Routes.UPLOAD_IMAGE) {
            UploadImageScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(Routes.DETAILS) {
            DetailsScreen(navController = navController)
        }
        composable(Routes.PAST_RECORDS) {
            PastRecordsScreen(navController = navController)
        }
    }
}
