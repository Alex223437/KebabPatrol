package com.example.kebabpatrol.presentation.screens

import android.Manifest
import android.annotation.SuppressLint
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.kebabpatrol.presentation.navigation.Screen
import com.example.kebabpatrol.presentation.viewmodel.KebabListViewModel
import com.google.android.gms.location.LocationServices
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import androidx.compose.foundation.layout.Column // <--- ВАЖНО
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.unit.dp


@Composable
fun KebabMapScreen(
    navController: NavController,
    viewModel: KebabListViewModel = hiltViewModel()
) {
    val kebabList by viewModel.kebabList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val context = LocalContext.current

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    // СТЕЙТ ДЛЯ ТВОЕЙ ТУШКИ (Где мы щас?)
    var userLocation by remember { mutableStateOf<GeoPoint?>(null) }
    var mapView by remember { mutableStateOf<MapView?>(null) }

    // ЦЕНТР ЗЛИНА (Чтоб карта сразу там открывалась, а не в Москве)
    val zlinCenter = GeoPoint(49.2275, 17.6705)

    @SuppressLint("MissingPermission")
    fun findMe() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val myPoint = GeoPoint(location.latitude, location.longitude)
                userLocation = myPoint
                // Плавненько едем к тебе
                mapView?.controller?.animateTo(myPoint)
                mapView?.controller?.setZoom(17.0)
            } else {
                Toast.makeText(context, "Включи GPS, немощь! Спутники слепые.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
            findMe()
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        )
    }

    remember {
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context))
        Configuration.getInstance().userAgentValue = "KebabPatrolApp"
        true
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
            }) {
                Icon(Icons.Default.MyLocation, contentDescription = "Где я?")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {

            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    MapView(ctx).apply {
                        setTileSource(TileSourceFactory.MAPNIK)
                        setMultiTouchControls(true)
                        controller.setZoom(14.0) // Чуть подальше, чтоб весь город видно было
                        controller.setCenter(zlinCenter) // СТАРТУЕМ В ЗЛИНЕ!
                        mapView = this
                    }
                },
                update = { map ->
                    map.overlays.clear()

                    // 1. РИСУЕМ ТЕБЯ (ЕСЛИ НАШЛИ)
                    userLocation?.let { loc ->
                        val userMarker = Marker(map)
                        userMarker.position = loc
                        userMarker.title = "ТУТ ПАХАН!"
                        userMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM) // Центруем, чтоб стрелка ровно стояла

                        // ВОТ ОНО! МЕНЯЕМ ШКУРУ!
                        // Берем нашу новую иконку ic_me
                        val myIcon = ContextCompat.getDrawable(context, com.example.kebabpatrol.R.drawable.ic_me)
                        userMarker.icon = myIcon

                        // Можно еще сделать, чтоб маркер нельзя было тыкать (он же не кебаб)
                        // userMarker.isDraggable = false // Ну это по дефолту

                        map.overlays.add(userMarker)
                    }

                    // 2. РИСУЕМ КЕБАБЫ (ПО НАСТОЯЩИМ КООРДИНАТАМ)
                    kebabList.forEach { kebab ->
                        // ВОТ ОНО! НИКАКОГО РАНДОМА! ЧИСТАЯ ПРАВДА!
                        val marker = Marker(map)
                        marker.position = GeoPoint(kebab.lat, kebab.lng)
                        marker.title = kebab.name
                        marker.snippet = "Рейтинг: ${kebab.rating} ⭐ | ${kebab.description.take(20)}..." // Кратко

                        marker.setOnMarkerClickListener { m, _ ->
                            m.showInfoWindow()
                            // Жмем на инфо-окно - летим в детали
                            navController.navigate(Screen.Details.withArgs(kebab.id.toString()))
                            true
                        }
                        map.overlays.add(marker)
                    }
                    map.invalidate()
                }
            )

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color.Black)
            }
        }
    }
}