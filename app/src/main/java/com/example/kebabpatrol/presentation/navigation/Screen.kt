package com.example.kebabpatrol.presentation.navigation

sealed class Screen(val route: String) {
    data object List : Screen("list_screen")
    data object Map : Screen("map")
    data object Details : Screen("details")
    data object LocationPicker : Screen("location_picker")
    object Splash : Screen("splash_screen")
    data object Add : Screen("add_kebab/{lat}/{lng}") {
        fun passLocation(lat: Double, lng: Double): String {
            return "add_kebab/$lat/$lng"
        }
    }

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}