package com.example.kebabpatrol.data.repository

import com.example.kebabpatrol.data.remote.KebabApi
import com.example.kebabpatrol.data.remote.dto.toDomain
import com.example.kebabpatrol.domain.repository.KebabRepository
import com.example.kebabpatrol.domain.model.KebabPlace
import javax.inject.Inject

// Завхоз (Hilt) подгоняет нам API
class KebabRepositoryImpl @Inject constructor(
    private val api: KebabApi
) : KebabRepository {

    // 1. ПОЛУЧИТЬ ВЕСЬ СПИСОК (ОБЩАК)
    override suspend fun getKebabs(): List<KebabPlace> {
        return try {
            // Зовем API (стучимся к барыге)
            val dtos = api.getKebabs()
            // Превращаем грязные DTO в чистые модели (отмываем бабки)
            dtos.map { it.toDomain() }
        } catch (e: Exception) {
            // Если API упал - возвращаем пустой список, чтоб приложуха не крашнулась
            e.printStackTrace()
            emptyList()
        }
    }

    // 2. ПОЛУЧИТЬ КОНКРЕТНЫЙ КЕБАБ ПО ID (ВЫДЕРНУТЬ ЧЕЛОВЕКА ИЗ ТОЛПЫ)
    override suspend fun getKebabById(id: Int): KebabPlace? {
        // ВАЖНО: Мы вызываем НАШУ ЖЕ функцию getKebabs(), а не выдуманную getAllKebabs()
        // Ищем в списке того, у кого id совпадает.
        return getKebabs().find { it.id == id }
    }
}