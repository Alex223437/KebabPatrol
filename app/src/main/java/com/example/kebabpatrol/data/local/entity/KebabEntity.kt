package com.example.kebabpatrol.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.kebabpatrol.domain.model.KebabPlace

@Entity(tableName = "kebabs")
data class KebabEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val rating: Double,
    val image: String,
    val lat: Double,
    val lng: Double
)

fun KebabEntity.toDomain(): KebabPlace {
    return KebabPlace(
        id = id,
        name = name,
        description = description,
        rating = rating,
        lat = lat,
        lng = lng,
        image = image
    )
}

fun KebabPlace.toEntity(): KebabEntity {
    return KebabEntity(
        id = id,
        name = name,
        description = description,
        rating = rating,
        lat = lat,
        lng = lng,
        image = image ?: "https://www.savorythoughts.com/wp-content/uploads/2021/09/Doner-Kebab-Recipe-Savory-Thoughts-8.jpg"
    )
}