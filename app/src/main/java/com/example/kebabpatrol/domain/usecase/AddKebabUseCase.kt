package com.example.kebabpatrol.domain.usecase

import com.example.kebabpatrol.domain.model.KebabPlace
import com.example.kebabpatrol.domain.repository.KebabRepository
import javax.inject.Inject

class AddKebabUseCase @Inject constructor(
    private val repository: KebabRepository
) {
    suspend operator fun invoke(name: String, description: String, rating: Double, lat: Double, lng: Double) {
        if (name.isBlank()) {
            throw Exception("Target name is required")
        }
        if (rating < 1.0 || rating > 5.0) {
            throw Exception("Rating must be between 1.0 and 5.0")
        }

        val kebab = KebabPlace(
            id = 0,
            name = name,
            description = description,
            rating = rating,
            lat = lat,
            lng = lng,
            image = "https://www.savorythoughts.com/wp-content/uploads/2021/09/Doner-Kebab-Recipe-Savory-Thoughts-8.jpg"
        )

        repository.insertKebab(kebab)
    }
}