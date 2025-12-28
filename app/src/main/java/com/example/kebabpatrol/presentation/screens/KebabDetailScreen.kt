package com.example.kebabpatrol.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.kebabpatrol.presentation.viewmodel.KebabDetailViewModel
import com.example.kebabpatrol.presentation.viewmodel.KebabState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KebabDetailScreen(
    navController: NavController,
    viewModel: KebabDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    // Scafold сам разберется с отступами от статус-бара (enableEdgeToEdge в Main работает на него)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ДОСЬЕ НА КЕБАБ 📂", maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFEEEEEE)
    ) { paddingValues ->
        // paddingValues СОДЕРЖИТ ВЫСОТУ TopAppBar + StatusBar.
        // Мы применяем их к Box, чтобы контент начинался СРАЗУ ПОД ПЛАШКОЙ.

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // <--- ВОТ ЭТО ВАЖНО
                .consumeWindowInsets(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val currentState = state) {
                is KebabState.Loading -> CircularProgressIndicator(color = Color.Black)

                is KebabState.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("КОСЯК: ${currentState.message}", color = Color.Red, modifier = Modifier.padding(16.dp))
                        Button(onClick = { navController.popBackStack() }) { Text("Назад") }
                    }
                }

                is KebabState.Success -> {
                    val kebab = currentState.kebab
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        AsyncImage(
                            model = kebab.image,
                            contentDescription = kebab.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp) // Картинка будет сразу под TopBar
                                .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)),
                            contentScale = ContentScale.Crop
                        )

                        // Остальной текст
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(kebab.name, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            Text("Рейтинг: ${kebab.rating} ⭐", fontSize = 20.sp, color = Color.Blue)
                            Text(kebab.description, color = Color.Gray, modifier = Modifier.padding(top = 8.dp))
                        }
                    }
                }
            }
        }
    }
}