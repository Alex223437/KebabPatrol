package com.example.kebabpatrol.presentation.screens

import android.preference.PreferenceManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kebabpatrol.presentation.navigation.Screen
import com.example.kebabpatrol.presentation.viewmodel.KebabListViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun KebabMapScreen(
    navController: NavController,
    viewModel: KebabListViewModel = hiltViewModel()
) {
    val kebabList by viewModel.kebabList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val context = LocalContext.current

    // 1. ИНИЦИАЛИЗАЦИЯ OSM (ВАЖНО! ИНАЧЕ БУДЕТ ПУСТО)
    // Это как предъявить пропуск на вахте.
    // Мы говорим серверу: "Мы не боты, мы честные пацаны".
    remember {
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context))
        // Важно задать User Agent, иначе забанят нахер
        Configuration.getInstance().userAgentValue = "KebabPatrolApp"
        true
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 2. ВПИХИВАЕМ ОБЫЧНУЮ VIEW В COMPOSE
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    MapView(ctx).apply {
                        setTileSource(TileSourceFactory.MAPNIK) // Стандартный вид карты
                        setMultiTouchControls(true) // Чтоб пальцами щипать можно было
                        controller.setZoom(12.0) // Зум
                        // Центр (Москва по дефолту, меняй на свой Мухосранск)
                        controller.setCenter(GeoPoint(55.751244, 37.618423))
                    }
                },
                update = { mapView ->
                    // 3. ОБНОВЛЕНИЕ МАРКЕРОВ
                    // Чистим старые метки, чтоб не двоилось в глазах
                    mapView.overlays.clear()

                    kebabList.forEach { kebab ->
                        // ОПЯТЬ ЖЕ, НУЖНЫ КООРДИНАТЫ В МОДЕЛИ!
                        // Пока ставлю рандом вокруг центра, чтоб ты увидел хоть что-то
                        // ЗАМЕНИ НА kebab.lat и kebab.lng
                        val lat = 55.75 + (Math.random() - 0.5) * 0.1
                        val lon = 37.61 + (Math.random() - 0.5) * 0.1

                        val marker = Marker(mapView)
                        marker.position = GeoPoint(lat, lon)
                        marker.title = kebab.name
                        marker.snippet = "Рейтинг: ${kebab.rating}"
                        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

                        // КЛИК ПО МАРКЕРУ
                        marker.setOnMarkerClickListener { m, map ->
                            m.showInfoWindow() // Показать окошко с инфой
                            // Жмем - летим в детали
                            navController.navigate(Screen.Details.withArgs(kebab.id.toString()))
                            true
                        }

                        mapView.overlays.add(marker)
                    }
                    // Перерисовываем карту
                    mapView.invalidate()
                }
            )

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Black
                )
            }
        }
    }
}