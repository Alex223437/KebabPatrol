package com.example.kebabpatrol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface // <--- Просто поверхность
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
import com.example.kebabpatrol.presentation.screens.KebabAddScreen // И ЭТО!
import com.example.kebabpatrol.presentation.screens.KebabLocationPickerScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Разрешаем лезть под статус-бар

        setContent {
            KebabPatrolTheme {
                val navController = rememberNavController()

                // ПРОСТО ПУСТОЙ КОНТЕЙНЕР, БЕЗ SCAFFOLD!
                Surface(modifier = Modifier.fillMaxSize()) {
                    NavHost(
                        navController = navController,
                        startDestination = Screen.List.route
                    ) {
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
                            route = Screen.Add.route, // Это "add_kebab/{lat}/{lng}"
                            arguments = listOf(
                                navArgument("lat") { type = NavType.FloatType }, // Float, потому что в URL double иногда глючит
                                navArgument("lng") { type = NavType.FloatType }
                            )
                        ) { backStackEntry ->
                            // Вытаскиваем координаты из рюкзака
                            val lat = backStackEntry.arguments?.getFloat("lat")?.toDouble() ?: 0.0
                            val lng = backStackEntry.arguments?.getFloat("lng")?.toDouble() ?: 0.0

                            // Открываем экран и отдаем ему координаты
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