package com.example.neuroview.screens

import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.neuroview.R
import com.example.neuroview.Routes
import com.example.neuroview.components.TopAppBar
import com.example.neuroview.ui.theme.NeuroViewTheme

@Composable
fun UploadImageScreen(navController: NavController) {
    val context = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            selectedImageUri = it
            Toast.makeText(context, "Image selected", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.Black,
                textColor = Color.White,
                logoSize = 42,
                titleFontSize = 28
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "UPLOAD",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF737373)
                    )
                    IconButton(
                        onClick = {
                            navController.navigate(Routes.DASHBOARD) {
                                popUpTo(Routes.DASHBOARD) { inclusive = false }
                            }
                        },
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF737373)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            selectedImageUri?.let { uri ->
                ImageInfoBox(uri = uri, onDelete = { selectedImageUri = null })
                Spacer(modifier = Modifier.height(16.dp))
            }

            DashedBorderBox(onClick = {
                imagePickerLauncher.launch("image/*")
            })

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { /* Start analysis */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedImageUri != null
            ) {
                Text("Start Analysis", color = Color.Black)
            }
        }
    }
}

@Composable
fun DashedBorderBox(onClick: () -> Unit) {
    val cornerRadius = 12.dp
    val boxHeight = 550.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(boxHeight)
            .padding(4.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val dashLength = 10.dp.toPx()
            val gapLength = 10.dp.toPx()
            drawRoundRect(
                color = Color.White,
                size = Size(size.width, size.height),
                cornerRadius = CornerRadius(cornerRadius.toPx(), cornerRadius.toPx()),
                style = Stroke(
                    width = 2.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(
                        floatArrayOf(dashLength, gapLength), 0f
                    )
                )
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_cloud_upload),
                contentDescription = "Upload Icon",
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = "Browse Files",
                fontSize = 16.sp,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Supported Formats: JPEG, PNG",
                fontSize = 12.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Upload Limit: 1 image file only.",
                fontSize = 12.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun ImageInfoBox(uri: Uri, onDelete: () -> Unit) {
    val context = LocalContext.current

    val fileName = remember(uri) {
        var name = "Unknown"
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (it.moveToFirst() && nameIndex != -1) {
                name = it.getString(nameIndex)
            }
        }
        name
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .border(width = 2.dp, color = Color.White, shape = MaterialTheme.shapes.medium)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = fileName,
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 1,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { onDelete() }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UploadImageScreenPreview() {
    NeuroViewTheme {
        UploadImageScreen(rememberNavController())
    }
}
