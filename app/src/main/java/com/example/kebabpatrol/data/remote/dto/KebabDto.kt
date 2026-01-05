package com.example.kebabpatrol.data.remote.dto

import com.example.kebabpatrol.domain.model.KebabPlace
import com.google.gson.annotations.SerializedName

data class KebabDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("rating") val rating: Double,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double,
    @SerializedName("image") val image: String?
)

fun KebabDto.toDomain(): KebabPlace {
    return KebabPlace(
        id = id.toIntOrNull() ?: 0, // Если ID не число, будет 0
        name = name,
        description = description,
        rating = rating,
        image = image ?: "https://www.savorythoughts.com/wp-content/uploads/2021/09/Doner-Kebab-Recipe-Savory-Thoughts-8.jpg",
        lat = lat,
        lng = lng
    )
}

fun KebabDto.toEntity(): com.example.kebabpatrol.data.local.entity.KebabEntity {
    return com.example.kebabpatrol.data.local.entity.KebabEntity(
        id = id.toIntOrNull() ?: 0,
        name = name,
        description = description,
        rating = rating,
        lat = lat,
        lng = lng,
        image = image ?: "https://www.savorythoughts.com/wp-content/uploads/2021/09/Doner-Kebab-Recipe-Savory-Thoughts-8.jpg"
    )
}