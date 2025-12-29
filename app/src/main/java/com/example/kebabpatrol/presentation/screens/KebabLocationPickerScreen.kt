package com.example.kebabpatrol.presentation.screens

import android.Manifest
import android.annotation.SuppressLint
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.kebabpatrol.presentation.navigation.Screen
import com.google.android.gms.location.LocationServices
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

@Composable
fun KebabLocationPickerScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var mapView by remember { mutableStateOf<MapView?>(null) }

    // Центр Злина (стартовая точка)
    val startPoint = GeoPoint(49.2275, 17.6705)

    // Функция поиска себя (копипаст, брат, но для дела можно)
    @SuppressLint("MissingPermission")
    fun findMe() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                mapView?.controller?.animateTo(GeoPoint(location.latitude, location.longitude))
                mapView?.controller?.setZoom(17.0)
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
            findMe()
        }
    }

    remember {
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context))
        Configuration.getInstance().userAgentValue = "KebabPatrolApp"
        true
    }

    Scaffold(
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {
                // КНОПКА "ГДЕ Я"
                FloatingActionButton(onClick = { permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)) }) {
                    Icon(Icons.Default.MyLocation, contentDescription = "Найти меня")
                }
                Spacer(modifier = Modifier.height(16.dp))

                // КНОПКА "ПОДТВЕРДИТЬ ВЫБОР" (ГАЛОЧКА)
                ExtendedFloatingActionButton(
                    onClick = {
                        mapView?.mapCenter?.let { center ->
                            // ЛЕТИМ ЗАПОЛНЯТЬ ДАННЫЕ С ЭТИМИ КООРДИНАТАМИ
                            navController.navigate(Screen.Add.passLocation(center.latitude, center.longitude))
                        }
                    },
                    containerColor = Color.Red,
                    contentColor = Color.White,
                    icon = { Icon(Icons.Default.Check, "Выбрать") },
                    text = { Text("ВЫБРАТЬ ЭТУ ТОЧКУ") }
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            // КАРТА
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    MapView(ctx).apply {
                        setTileSource(TileSourceFactory.MAPNIK)
                        setMultiTouchControls(true)
                        controller.setZoom(14.0)
                        controller.setCenter(startPoint)
                        mapView = this
                    }
                }
            )

            // ПРИЦЕЛ ПО ЦЕНТРУ (НЕПОДВИЖНЫЙ)
            Icon(
                imageVector = Icons.Default.AddLocation,
                contentDescription = "Прицел",
                tint = Color.Red,
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.Center)
                    .padding(bottom = 24.dp) // Чуть выше центра, чтоб ножка стояла в точке
            )

            // ПОДСКАЗКА СВЕРХУ
            Surface(
                modifier = Modifier.padding(16.dp).align(Alignment.TopCenter),
                shape = MaterialTheme.shapes.medium,
                color = Color.Black.copy(alpha = 0.7f)
            ) {
                Text(
                    text = "Наведи прицел на шаурму!",
                    color = Color.White,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}