package com.example.kebabpatrol.domain.model

import android.media.Image

data class KebabPlace(
    val id: Int,
    val name: String,         // Погоняло (Название)
    val description: String,  // За что сидит (Описание)
    val rating: Double,       // Авторитет (Рейтинг)
    val image: String,
    val isFavorite: Boolean = false, // В уважухе или нет
    val lat: Double, // <--- ЭТИ ДВА ПАССАЖИРА ОБЯЗАНЫ БЫТЬ!
    val lng: Double
)