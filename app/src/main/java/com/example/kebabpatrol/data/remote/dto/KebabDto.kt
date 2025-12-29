package com.example.kebabpatrol.data.remote.dto

import com.example.kebabpatrol.domain.model.KebabPlace
import com.google.gson.annotations.SerializedName

// Это то, что прилетает с интернета. Имена полей должны совпадать с MockAPI!
data class KebabDto(
    @SerializedName("id") val id: String, // MockAPI часто шлет ID как строку
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("rating") val rating: Double,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double,
    @SerializedName("image") val image: String?
    // Картинку пока опустим, если не добавлял, или добавь val image: String
)

// Функция-преобразователь. Из грязного DTO делаем чистую модель для экрана.
fun KebabDto.toDomain(): KebabPlace {
    return KebabPlace(
        id = id.toIntOrNull() ?: 0, // Если ID не число, будет 0
        name = name,
        description = description,
        rating = rating,
        image = image ?: "https://www.savorythoughts.com/wp-content/uploads/2021/09/Doner-Kebab-Recipe-Savory-Thoughts-8.jpg",
        lat = lat,
        lng = lng
        // isFavorite тут нет, оно локальное
    )
}