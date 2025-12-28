package com.example.kebabpatrol.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons // <--- НЕ ЗАБУДЬ ЭТО
import androidx.compose.material.icons.filled.LocationOn // <--- И ЭТО ДЛЯ ЗНАЧКА
import androidx.compose.material3.* // <--- ТУТ ВСЕ МАТЕРИАЛЫ
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
import com.example.kebabpatrol.domain.model.KebabPlace
import com.example.kebabpatrol.presentation.viewmodel.KebabListViewModel

@Composable
fun KebabListScreen(
    navController: NavController,
    viewModel: KebabListViewModel = hiltViewModel()
) {
    val kebabList by viewModel.kebabList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // ВОТ ОН, БАТЯ SCAFFOLD! ДЕРЖИТ ЭКРАН В РАМКАХ!
    Scaffold(
        // КНОПКА КАРТЫ (FAB) - ТЕПЕРЬ ОНА ЕСТЬ!
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("map") }, // ЛЕТИМ НА КАРТУ
                containerColor = Color.Black, // ЧЕРНАЯ, КАК ДУША МЕНТА
                contentColor = Color.White
            ) {
                Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Карта")
            }
        }
    ) { paddingValues -> // paddingValues - ЭТО ВАЖНО, ЧТОБ СПИСОК НЕ ЗАЛЕЗ ПОД КНОПКУ

        // ТЕПЕРЬ ТВОЙ COLUMN ВНУТРИ
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFEEEEEE))
                .padding(paddingValues) // <--- ВОТ ТУТ МЫ УВАЖАЕМ ГРАНИЦЫ
        ) {
            Text(
                text = "КЕБАБ ПАТРУЛЬ 🚔",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            // ОШИБКА
            if (error != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = error!!, color = Color.Red, fontWeight = FontWeight.Bold)
                }
            }

            // КРУТИЛКА
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.Black)
                }
            }

            // СПИСОК
            LazyColumn(
                contentPadding = PaddingValues(bottom = 80.dp, top = 16.dp, start = 16.dp, end = 16.dp), // ОТСТУП СНИЗУ ПОБОЛЬШЕ, ЧТОБ КНОПКА НЕ ПЕРЕКРЫВАЛА ПОСЛЕДНИЙ КЕБАБ
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(kebabList) { kebab ->
                    KebabItem(kebab = kebab) {
                        // ВНИМАНИЕ, НЕМОЩЬ!!! ВОТ ТУТ МЫ ПЕРЕДАЕМ ID!!!
                        // НЕ ПРОСТО "details", А "details/5" НАПРИМЕР
                        navController.navigate("details/${kebab.id}")
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
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {

            AsyncImage(
                model = kebab.image,
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(text = kebab.name, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Text(
                    text = "Рейтинг: ${kebab.rating}",
                    color = if (kebab.rating > 4.0) Color(0xFF006400) else Color.Red,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = kebab.description,
                    color = Color.Gray,
                    maxLines = 2
                )
            }
        }
    }
}