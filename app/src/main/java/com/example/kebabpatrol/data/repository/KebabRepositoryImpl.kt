package com.example.kebabpatrol.data.repository

import com.example.kebabpatrol.data.local.KebabDao
import com.example.kebabpatrol.data.local.entity.toDomain
import com.example.kebabpatrol.data.local.entity.toEntity

import com.example.kebabpatrol.data.remote.KebabApi
import com.example.kebabpatrol.data.remote.dto.toEntity
import com.example.kebabpatrol.domain.model.KebabPlace
import com.example.kebabpatrol.domain.repository.KebabRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class KebabRepositoryImpl @Inject constructor(
    private val api: KebabApi,
    private val dao: KebabDao
) : KebabRepository {

    override fun getKebabs(): Flow<List<KebabPlace>> {
        return dao.getAllKebabs()
            .map { entities ->
                entities.map { it.toDomain() }
            }
            .onStart {
                try {
                    val remoteKebabs = api.getKebabs()
                    dao.insertKebabs(remoteKebabs.map { it.toEntity() })
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
    }

    override suspend fun getKebabById(id: Int): KebabPlace? {
        return dao.getKebabById(id)?.toDomain()
    }

    override suspend fun insertKebab(kebab: KebabPlace) {
        dao.insertKebab(kebab.toEntity())
    }
}