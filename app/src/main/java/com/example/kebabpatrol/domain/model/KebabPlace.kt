package com.example.kebabpatrol.domain.model

import android.media.Image

data class KebabPlace(
    val id: Int,
    val name: String,
    val description: String,
    val rating: Double,
    val image: String,
    val isFavorite: Boolean = false,
    val lat: Double,
    val lng: Double
)