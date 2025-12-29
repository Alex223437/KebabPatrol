package com.example.kebabpatrol.presentation.screens

import android.Manifest
import android.annotation.SuppressLint
import android.preference.PreferenceManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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

    var isMapInitialized by remember { mutableStateOf(false) }

    val startPoint = GeoPoint(49.2275, 17.6705)

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
        containerColor = Color(0xFF121212),
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {
                FloatingActionButton(
                    onClick = { permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)) },
                    containerColor = Color.White,
                    contentColor = Color.Black,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Icon(Icons.Default.MyLocation, contentDescription = "Find Me")
                }

                Spacer(modifier = Modifier.height(16.dp))

                FloatingActionButton(
                    onClick = {
                        mapView?.mapCenter?.let { center ->
                            navController.navigate(Screen.Add.passLocation(center.latitude, center.longitude))
                        }
                    },
                    containerColor = Color(0xFFD32F2F),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = "Confirm Location")
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    MapView(ctx).apply {
                        setTileSource(TileSourceFactory.MAPNIK)
                        setMultiTouchControls(true)
                        mapView = this
                    }
                },
                update = { map ->
                    if (!isMapInitialized) {
                        map.controller.setZoom(17.0)
                        map.controller.setCenter(startPoint)
                        isMapInitialized = true
                    }
                }
            )

            Icon(
                imageVector = Icons.Default.AddLocation,
                contentDescription = "Target",
                tint = Color(0xFFD32F2F),
                modifier = Modifier
                    .size(96.dp)
                    .align(Alignment.Center)
                    .padding(bottom = 48.dp)
            )

            Surface(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .align(Alignment.TopCenter)
                    .border(1.dp, Color(0xFF333333)),
                shape = RectangleShape,
                color = Color(0xFF212121).copy(alpha = 0.9f)
            ) {
                Text(
                    text = "AIM AT TARGET",
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}