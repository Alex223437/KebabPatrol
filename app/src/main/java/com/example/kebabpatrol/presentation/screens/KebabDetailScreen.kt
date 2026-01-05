package com.example.kebabpatrol.presentation.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.kebabpatrol.domain.model.KebabPlace
import com.example.kebabpatrol.presentation.viewmodel.KebabDetailViewModel
import com.example.kebabpatrol.presentation.viewmodel.KebabState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KebabDetailScreen(
    navController: NavController,
    viewModel: KebabDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        (state as? KebabState.Success)?.kebab?.name ?: "?",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF121212),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFF121212),
        bottomBar = {
            if (state is KebabState.Success) {
                val kebab = (state as KebabState.Success).kebab
                Button(
                    onClick = {
                        val gmmIntentUri = Uri.parse("geo:${kebab.lat},${kebab.lng}?q=${kebab.lat},${kebab.lng}(${kebab.name})")
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        mapIntent.setPackage("com.google.android.apps.maps")
                        if (mapIntent.resolveActivity(context.packageManager) != null) {
                            context.startActivity(mapIntent)
                        } else {
                            context.startActivity(Intent(Intent.ACTION_VIEW, gmmIntentUri))
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                    shape = RectangleShape
                ) {
                    Icon(Icons.Default.Map, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("LOCATE TARGET", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val currentState = state) {
                is KebabState.Loading -> CircularProgressIndicator(color = Color(0xFFD32F2F))

                is KebabState.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFD32F2F), modifier = Modifier.size(48.dp))
                        Text("DATA CORRUPTED: ${currentState.message}", color = Color(0xFFD32F2F), modifier = Modifier.padding(16.dp), fontFamily = FontFamily.Monospace)
                        Button(
                            onClick = { navController.popBackStack() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RectangleShape
                        ) {
                            Text("ABORT", color = Color.Black)
                        }
                    }
                }

                is KebabState.Success -> {
                    val kebab = currentState.kebab
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        Box {
                            AsyncImage(
                                model = kebab.image,
                                contentDescription = kebab.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp),
                                contentScale = ContentScale.Crop
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                                    .background(Color.Black.copy(alpha = 0.3f))
                            )

                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .background(Color(0xFFD32F2F))
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = if (kebab.rating >= 4.0) "STATUS: APPROVED" else "STATUS: HAZARD",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }

                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = kebab.name.uppercase(),
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                lineHeight = 36.sp
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 12.dp)
                            ) {
                                Text(
                                    text = "${kebab.rating}",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFFC107)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "★★★★★",
                                    fontSize = 24.sp,
                                    color = Color(0xFFFFC107)
                                )
                            }

                            HorizontalDivider(color = Color(0xFF333333), thickness = 2.dp)

                            Column(
                                modifier = Modifier
                                    .padding(vertical = 16.dp)
                                    .fillMaxWidth()
                                    .background(Color(0xFF212121))
                                    .border(1.dp, Color(0xFF333333))
                                    .padding(16.dp)
                            ) {
                                Text("TACTICAL DATA", color = Color(0xFF757575), fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("LAT: ${kebab.lat}", color = Color.White, fontFamily = FontFamily.Monospace)
                                Text("LNG: ${kebab.lng}", color = Color.White, fontFamily = FontFamily.Monospace)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("INTEL:", color = Color(0xFF757575), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = kebab.description,
                                color = Color(0xFFE0E0E0),
                                fontSize = 16.sp,
                                fontFamily = FontFamily.Monospace,
                                lineHeight = 24.sp
                            )

                            Spacer(modifier = Modifier.height(100.dp))
                        }
                    }
                }
            }
        }
    }
}