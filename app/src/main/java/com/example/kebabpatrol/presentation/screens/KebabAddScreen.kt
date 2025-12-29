package com.example.kebabpatrol.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kebabpatrol.presentation.viewmodel.KebabAddViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KebabAddScreen(
    navController: NavController,
    // Принимаем координаты, где мы стоим (передадим из навигации)
    lat: Double,
    lng: Double,
    viewModel: KebabAddViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("СДАТЬ ТОЧКУ \uD83D\uDCCD") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ПОЛЯ ВВОДА
            OutlinedTextField(
                value = viewModel.name.value,
                onValueChange = viewModel::onNameChange,
                label = { Text("Название шаурмы") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = viewModel.description.value,
                onValueChange = viewModel::onDescriptionChange,
                label = { Text("Поясни за шмот (Описание)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = viewModel.rating.value,
                onValueChange = viewModel::onRatingChange,
                label = { Text("Рейтинг (1-5)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // КНОПКА
            Button(
                onClick = {
                    viewModel.saveKebab(lat, lng) {
                        // Если успех - валим назад
                        navController.popBackStack()
                    }
                },
                enabled = !viewModel.isLoading.value,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (viewModel.isLoading.value) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp), // Размер задаем здесь
                        color = Color.White,
                        strokeWidth = 2.dp // Толщина линии (опционально)
                    )
                } else {
                    Text("ЗАФИКСИРОВАТЬ")
                }
            }
        }
    }
}