package com.example.kebabpatrol.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.kebabpatrol.domain.model.KebabPlace
import com.example.kebabpatrol.presentation.navigation.Screen
import com.example.kebabpatrol.presentation.viewmodel.KebabListViewModel

@Composable
fun KebabListScreen(
    navController: NavController,
    viewModel: KebabListViewModel = hiltViewModel()
) {
    val kebabList by viewModel.kebabList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Scaffold(
        containerColor = Color(0xFF121212),
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {

                FloatingActionButton(
                    onClick = { navController.navigate(Screen.Map.route) },
                    containerColor = Color.White,
                    contentColor = Color(0xFF212121),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Icon(Icons.Default.Map, contentDescription = "Open Map")
                }

                Spacer(modifier = Modifier.height(16.dp))

                FloatingActionButton(
                    onClick = { navController.navigate(Screen.LocationPicker.route) },
                    containerColor = Color(0xFFD32F2F),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Kebab")
                }

            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF121212))
                .padding(paddingValues)
        ) {
            Text(
                text = "KEBAB PATROL 🚔",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.Black,
                color = Color.White
            )

            if (error != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = error!!, color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold)
                }
            }

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFFD32F2F))
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(bottom = 100.dp, top = 8.dp, start = 16.dp, end = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(kebabList) { kebab ->
                    KebabItem(kebab = kebab) {
                        navController.navigate(Screen.Details.withArgs(kebab.id.toString()))
                    }
                }
            }
        }
    }
}

@Composable
fun KebabItem(kebab: KebabPlace, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF212121))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            AsyncImage(
                model = kebab.image,
                contentDescription = null,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(0.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = kebab.name.uppercase(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${kebab.rating} 👊",
                    color = Color(0xFFFFC107),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = kebab.description,
                    color = Color(0xFFBDBDBD),
                    maxLines = 2,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}