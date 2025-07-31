package com.example.neuroview.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.neuroview.Routes
import com.example.neuroview.components.BottomNavigationBar
import com.example.neuroview.components.TopAppBar
import com.example.neuroview.R // Make sure R is imported to access drawables
import com.example.neuroview.data.TumorData

// Accompanist Pager imports
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.pager.HorizontalPagerIndicator

@OptIn(ExperimentalPagerApi::class) // Needed for Accompanist Pager
@Composable
fun DashboardScreen(navController: NavController) {
    // Get tumor data
    val tumors = TumorData.getAllTumors()

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
                currentRoute = Routes.DASHBOARD,
                bottomPadding = 18,
                navBarHeight = 80,
                regularIconSize = 32,
                fabIconSize = 32,
                fabSize = 64,
                navBarWidth = 0.8f,
                horizontalPadding = 16
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(paddingValues), // Apply scaffold padding
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // "LEARN" Text
            Text(
                text = "LEARN",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF737373), // This color matches the grey in the image
                modifier = Modifier.padding(top = 20.dp, bottom = 40.dp)
            )

            val pagerState = rememberPagerState(initialPage = 0)

            // Carousel / Horizontal Pager
            HorizontalPager(
                count = tumors.size,
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 32.dp),
                itemSpacing = 16.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) { page ->
                val tumor = tumors[page]

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .background(Color.Transparent, RoundedCornerShape(16.dp))
                        .clickable {
                            navController.navigate(Routes.tumorDetail(tumor.name))
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background( Color(0xFF0B0B0B)) // This is the background of the box containing the border and content
                            .padding(2.dp) // This creates the white border
                            .background( Color(0xFF0B0B0B), RoundedCornerShape(16.dp)) // This is the actual white border and background for content
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Image(
                                painter = painterResource(id = tumor.imageResource),
                                contentDescription = "${tumor.name} image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                            )

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White)
                                    .padding(horizontal = 16.dp, vertical = 12.dp)
                                    .weight(0.3f),
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = tumor.name,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_readmore),
                                        contentDescription = "Go to details",
                                        tint = Color.Black,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp)) // Space between pager and indicator

            // Pager Indicator
            HorizontalPagerIndicator(
                pagerState = pagerState,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                activeColor = Color.White,
                inactiveColor = Color.Gray,
                indicatorWidth = 8.dp,
                indicatorHeight = 8.dp,
                spacing = 8.dp
            )

            // --- ADDED TEXTS BELOW PAGER INDICATOR ---
            Spacer(modifier = Modifier.height(50.dp)) // Space after indicator before new texts

            // "Our brain is one of the most important and complex parts of the human body."
            Text(
                text = buildAnnotatedString {
                    append("Our brain is one of the most ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("important and complex parts of the human body.")
                    }
                },
                fontSize = 20.sp, // Adjusted font size
                fontWeight = FontWeight.SemiBold, // Adjust font weight as per image
                color = Color.White,
                textAlign = TextAlign.Center, // Centered text as in image
                modifier = Modifier
                    .fillMaxWidth(0.85f) // Adjust width to match image visually
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(20.dp)) // Space between sentences

            // "It controls how we think, feel, move, and live. That's why taking care of it isn't optional — it's essential."
            Text(
                text = buildAnnotatedString {
                    append("It controls how we think, feel, move, and live. That's why taking care of it isn't optional — it's ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("essential.")
                    }
                },
                fontSize = 20.sp, // Consistent font size
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                textAlign = TextAlign.Center, // Centered text
                modifier = Modifier
                    .fillMaxWidth(0.85f) // Consistent width with the text above
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp)) // Bottom padding
        }
    }
}

// Preview function for DashboardScreen
@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    // Mock NavController for preview
    val mockNavController = rememberNavController()

    DashboardScreen(navController = mockNavController)
}