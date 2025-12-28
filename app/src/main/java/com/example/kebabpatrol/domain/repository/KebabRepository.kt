package com.example.kebabpatrol.domain.repository

import com.example.kebabpatrol.domain.model.KebabPlace

interface KebabRepository {
    suspend fun getKebabs(): List<KebabPlace>
    suspend fun getKebabById(id: Int): KebabPlace?
}