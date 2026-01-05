package com.example.kebabpatrol.presentation.screens

import android.Manifest
import android.annotation.SuppressLint
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
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
import androidx.compose.ui.unit.dp
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

@Composable
fun KebabMapScreen(
    navController: NavController,
    viewModel: KebabListViewModel = hiltViewModel()
) {
    val kebabList by viewModel.kebabList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val context = LocalContext.current

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    var userLocation by remember { mutableStateOf<GeoPoint?>(null) }
    var mapView by remember { mutableStateOf<MapView?>(null) }

    val startPoint = GeoPoint(49.2275, 17.6705)

    @SuppressLint("MissingPermission")
    fun findMe() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val myPoint = GeoPoint(location.latitude, location.longitude)
                userLocation = myPoint
                mapView?.controller?.animateTo(myPoint)
                mapView?.controller?.setZoom(17.0)
            } else {
                Toast.makeText(context, "Enable GPS to find your location.", Toast.LENGTH_SHORT).show()
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
        containerColor = Color(0xFF121212),
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(Screen.List.route)
                    },
                    containerColor = Color(0xFF212121),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Back to List")
                }

                Spacer(modifier = Modifier.height(16.dp))

                FloatingActionButton(
                    onClick = {
                        permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
                    },
                    containerColor = Color(0xFFD32F2F),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Icon(Icons.Default.MyLocation, contentDescription = "Find Me")
                }
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
                        controller.setZoom(14.0)
                        controller.setCenter(startPoint)
                        mapView = this
                    }
                },
                update = { map ->
                    val fireIcon = ContextCompat.getDrawable(context, com.example.kebabpatrol.R.drawable.ic_marker_fire)
                    val goldIcon = ContextCompat.getDrawable(context, com.example.kebabpatrol.R.drawable.ic_marker_gold)
                    map.overlays.clear()

                    userLocation?.let { loc ->
                        val userMarker = Marker(map)
                        userMarker.position = loc
                        userMarker.title = "MY LOCATION"
                        userMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

                        val myIcon = ContextCompat.getDrawable(context, com.example.kebabpatrol.R.drawable.ic_me)
                        userMarker.icon = myIcon

                        map.overlays.add(userMarker)
                    }

                    kebabList.forEach { kebab ->
                        val marker = Marker(map)
                        marker.position = GeoPoint(kebab.lat, kebab.lng)
                        marker.title = kebab.name

                        if (kebab.rating >= 4.5) {
                            marker.icon = goldIcon
                            marker.snippet = "ELITE TIER! Rating: ${kebab.rating} 👑"
                            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                        } else {
                            marker.icon = fireIcon
                            marker.snippet = "SOLID CHOICE. Rating: ${kebab.rating} 🔥"
                            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        }

                        marker.setOnMarkerClickListener { m, _ ->
                            m.showInfoWindow()
                            navController.navigate(Screen.Details.withArgs(kebab.id.toString()))
                            true
                        }
                        map.overlays.add(marker)
                    }
                    map.invalidate()
                }
            )

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFFD32F2F)
                )
            }
        }
    }
}