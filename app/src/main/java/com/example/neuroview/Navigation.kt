package com.example.neuroview

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.neuroview.screens.DashboardScreen
import com.example.neuroview.screens.DetailsScreen
import com.example.neuroview.screens.HomeScreen
import com.example.neuroview.screens.PastRecordsScreen
import com.example.neuroview.screens.UploadImageScreen
import com.example.neuroview.screens.TumorDetailScreen

object Routes {
    const val HOME = "home"
    const val DASHBOARD = "dashboard"
    const val UPLOAD_IMAGE = "upload_image"
    const val DETAILS = "details"
    const val PAST_RECORDS = "past_records"
    const val RESULT = "result"
    const val RESULT_WITH_DATA = "result?predictionJson={predictionJson}&imageUri={imageUri}"
    const val TUMOR_DETAIL = "tumor_detail"

    fun tumorDetail(tumorName: String) = "tumor_detail/$tumorName"
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
            UploadImageScreen(navController = navController)
        }
        composable(Routes.DETAILS) {
            DetailsScreen(navController = navController)
        }
        composable(Routes.PAST_RECORDS) {
            PastRecordsScreen(navController = navController)
        }
        composable(Routes.RESULT) {
            com.example.neuroview.screens.ResultScreen(
                navController = navController,
                predictionJson = null,
                imageUri = null
            )
        }
        composable(
            route = Routes.RESULT_WITH_DATA,
            arguments = listOf(
                navArgument("predictionJson") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument("imageUri") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val predictionJson = backStackEntry.arguments?.getString("predictionJson")
            val imageUri = backStackEntry.arguments?.getString("imageUri")
            com.example.neuroview.screens.ResultScreen(
                navController = navController,
                predictionJson = predictionJson,
                imageUri = imageUri
            )
        }
        composable(
            route = "tumor_detail/{tumorName}",
            arguments = listOf(navArgument("tumorName") { type = NavType.StringType })
        ) { backStackEntry ->
            val tumorName = backStackEntry.arguments?.getString("tumorName")
            TumorDetailScreen(
                navController = navController,
                tumorName = tumorName
            )
        }
    }
}
