package com.example.kebabpatrol.domain.usecase

import com.example.kebabpatrol.domain.repository.KebabRepository
import javax.inject.Inject

class AddKebabUseCase @Inject constructor(
    private val repository: KebabRepository
) {
    // Вызываем как функцию: addKebabUseCase(...)
    suspend operator fun invoke(name: String, description: String, rating: Double, lat: Double, lng: Double) {
        if (name.isBlank()) throw Exception("Название не может быть пустым, э!")
        if (rating < 1 || rating > 5) throw Exception("Рейтинг от 1 до 5, не дури!")

        repository.addKebab(name, description, rating, lat, lng)
    }
}