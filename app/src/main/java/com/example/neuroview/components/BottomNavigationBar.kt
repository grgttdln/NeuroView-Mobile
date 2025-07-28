package com.example.neuroview.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.neuroview.Routes
import com.example.neuroview.R

// MODIFIED: BottomNavItem now holds two icon resource IDs
data class BottomNavItem(
    val route: String,
    val unselectedIcon: Int, // Icon for unselected state
    val selectedIcon: Int,   // Icon for selected (filled) state
    val isCentralButton: Boolean = false
)

@Composable
fun BottomNavigationBar(
    navController: NavController,
    currentRoute: String?,
    bottomPadding: Int = 16,
    navBarHeight: Int = 80,
    regularIconSize: Int = 32,
    fabIconSize: Int = 32,
    fabSize: Int = 64,
    navBarWidth: Float = 0.6f,
    horizontalPadding: Int = 16
) {
    val items = listOf(
        // MODIFIED: Provide both unselected and selected icon for Home
        BottomNavItem(Routes.DASHBOARD, R.drawable.ic_home, R.drawable.ic_home_filled),
        // MODIFIED: For the central button, unselected and selected icons are the same
        BottomNavItem(Routes.UPLOAD_IMAGE, R.drawable.ic_cloud_upload, R.drawable.ic_cloud_upload, isCentralButton = true),
        // MODIFIED: Provide both unselected and selected icon for Folder
        BottomNavItem(Routes.PAST_RECORDS, R.drawable.ic_folder, R.drawable.ic_folder_filled)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(navBarHeight.dp)
            .background(Color.Transparent)
            .padding(bottom = bottomPadding.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(navBarWidth)
                .height(56.dp)
                .align(Alignment.BottomCenter)
                .padding(horizontal = horizontalPadding.dp),
            color = Color.White,
            shape = RoundedCornerShape(28.dp),
            shadowElevation = 8.dp
        ) {
            // Empty surface for background
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(navBarWidth)
                .padding(horizontal = horizontalPadding.dp)
                .height(navBarHeight.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                if (item.isCentralButton) {
                    Spacer(modifier = Modifier.weight(0.5f))

                    CustomElevatedFab(
                        icon = item.unselectedIcon, // Central button's icon doesn't change
                        isSelected = currentRoute == item.route,
                        fabSize = fabSize,
                        iconSize = fabIconSize,
                        onClick = {
                            if (currentRoute != item.route) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )

                    Spacer(modifier = Modifier.weight(0.5f))
                } else {
                    CustomBottomNavItem(
                        item = item,
                        isSelected = currentRoute == item.route,
                        iconSize = regularIconSize,
                        onClick = {
                            if (currentRoute != item.route) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun CustomBottomNavItem(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Int = 24
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // MODIFIED: Choose icon based on selection state
        val iconToDisplay = if (isSelected) item.selectedIcon else item.unselectedIcon
        Icon(
            painter = painterResource(id = iconToDisplay),
            contentDescription = null,
            tint = if (isSelected) Color.Black else Color.Black,
            modifier = Modifier.size(iconSize.dp)
        )
    }
}

@Composable
fun CustomElevatedFab(
    icon: Int, // This icon will be the same regardless of selection for the FAB
    isSelected: Boolean, // Kept for consistency, though not used to change FAB icon
    onClick: () -> Unit,
    fabSize: Int = 64,
    iconSize: Int = 28
) {
    Card(
        modifier = Modifier
            .size(fabSize.dp)
            .offset(y = (-20).dp),
        shape = CircleShape,
        colors = CardDefaults.cardColors(containerColor = Color.Black),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(iconSize.dp)
            )
        }
    }
}