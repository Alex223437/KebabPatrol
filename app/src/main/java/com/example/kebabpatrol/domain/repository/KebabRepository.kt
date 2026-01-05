package com.example.kebabpatrol.domain.repository

import com.example.kebabpatrol.domain.model.KebabPlace
import kotlinx.coroutines.flow.Flow

interface KebabRepository {
    fun getKebabs(): Flow<List<KebabPlace>>
    suspend fun getKebabById(id: Int): KebabPlace?
    suspend fun insertKebab(kebab: KebabPlace)
}