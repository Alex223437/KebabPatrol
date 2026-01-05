package com.example.kebabpatrol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kebabpatrol.presentation.navigation.Screen
import com.example.kebabpatrol.presentation.screens.KebabDetailScreen
import com.example.kebabpatrol.presentation.screens.KebabListScreen
import com.example.kebabpatrol.presentation.screens.KebabMapScreen
import com.example.kebabpatrol.ui.theme.KebabPatrolTheme
import dagger.hilt.android.AndroidEntryPoint
import com.example.kebabpatrol.presentation.screens.KebabAddScreen
import com.example.kebabpatrol.presentation.screens.KebabLocationPickerScreen
import com.example.kebabpatrol.presentation.screens.KebabSplashScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            KebabPatrolTheme {
                val navController = rememberNavController()

                Surface(modifier = Modifier.fillMaxSize()) {
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Splash.route
                    ) {

                        composable(Screen.Splash.route) {
                            KebabSplashScreen(navController = navController)
                        }

                        composable(Screen.List.route) {
                            KebabListScreen(navController)
                        }

                        composable(Screen.Map.route) {
                            KebabMapScreen(navController)
                        }

                        composable(
                            route = "details/{kebabId}",
                            arguments = listOf(navArgument("kebabId") { type = NavType.IntType })
                        ) {
                            KebabDetailScreen(navController)
                        }

                        composable(
                            route = Screen.Add.route,
                            arguments = listOf(
                                navArgument("lat") { type = NavType.FloatType },
                                navArgument("lng") { type = NavType.FloatType }
                            )
                        ) { backStackEntry ->
                            val lat = backStackEntry.arguments?.getFloat("lat")?.toDouble() ?: 0.0
                            val lng = backStackEntry.arguments?.getFloat("lng")?.toDouble() ?: 0.0
                            KebabAddScreen(navController = navController, lat = lat, lng = lng)
                        }

                        composable(Screen.LocationPicker.route) {
                            KebabLocationPickerScreen(navController)
                        }
                    }
                }
            }
        }
    }
}