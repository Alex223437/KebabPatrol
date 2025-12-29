package com.example.kebabpatrol.presentation.navigation

// ЭТО ТВОИ ПОНЯТИЯ. ЭТО ИМЕНА ТВОИХ ХАТ.
sealed class Screen(val route: String) {
    // 1. СПИСОК (Главная хата)
    data object List : Screen("list_screen")

    // 2. КАРТА (Прогулочный дворик) - ВОТ ОНА, РОДНАЯ!
    // Если ты тут напишешь "map", то и в NavHost лови "map"!
    data object Map : Screen("map")

    // 3. ДЕТАЛИ (Изолятор)
    // Тут только база, без аргументов
    data object Details : Screen("details")

    data object LocationPicker : Screen("location_picker")

    data object Add : Screen("add_kebab/{lat}/{lng}") {
        // Функция, чтобы удобно вызывать: Screen.Add.passLocation(55.0, 37.0)
        fun passLocation(lat: Double, lng: Double): String {
            return "add_kebab/$lat/$lng"
        }
    }

    // Вспомогательная хрень, чтоб аргументы (ID) приклеивать
    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}