package com.example.kebabpatrol.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.kebabpatrol.domain.model.KebabPlace

// ТАБЛИЦА "kebabs" В БАЗЕ
@Entity(tableName = "kebabs")
data class KebabEntity(
    @PrimaryKey val id: String, // ID должен быть уникальным!
    val name: String,
    val description: String,
    val rating: Double,
    val image: String,
    val lat: Double,
    val lng: Double
) {
    // Превращаемся в чистую модель для UI
    fun toDomain(): KebabPlace {
        return KebabPlace(
            id = id.toIntOrNull() ?: 0, // Если id строка, а в модели Int - конвертим
            name = name,
            description = description,
            rating = rating,
            image = image,
            lat = lat,
            lng = lng
        )
    }
}

// Превращаем грязный DTO (с сервера) в Энтити (для базы)
// (Это расширение можно кинуть в мапперы)
fun com.example.kebabpatrol.data.remote.dto.KebabDto.toEntity(): KebabEntity {
    return KebabEntity(
        id = id,
        name = name,
        description = description,
        rating = rating,
        image = image ?: "https://www.savorythoughts.com/wp-content/uploads/2021/09/Doner-Kebab-Recipe-Savory-Thoughts-8.jpg",
        lat = lat,
        lng = lng
    )
}