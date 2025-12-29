package com.example.kebabpatrol.data.repository

import com.example.kebabpatrol.data.local.KebabDao
import com.example.kebabpatrol.data.local.entity.toEntity
import com.example.kebabpatrol.data.remote.KebabApi
import com.example.kebabpatrol.data.remote.dto.KebabDto
import com.example.kebabpatrol.domain.model.KebabPlace
import com.example.kebabpatrol.domain.repository.KebabRepository
import javax.inject.Inject

class KebabRepositoryImpl @Inject constructor(
    private val api: KebabApi,
    private val dao: KebabDao // <--- ПОДТЯНУЛИ ЗАВХОЗА С КЛЮЧАМИ ОТ ПОДВАЛА
) : KebabRepository {

    // 1. ПОЛУЧИТЬ СПИСОК (СНАЧАЛА ОБНОВЛЯЕМ БАЗУ, ПОТОМ ЧИТАЕМ)
    override suspend fun getKebabs(): List<KebabPlace> {
        // Шаг А: Пытаемся обновить общак через инет
        try {
            val remoteKebabs = api.getKebabs() // Стучимся к барыге
            // Если инет есть и барыга ответил:
            dao.clearKebabs() // Выкидываем старье
            dao.insertKebabs(remoteKebabs.map { it.toEntity() }) // Кладем свежак в сейф
        } catch (e: Exception) {
            // Если инета нет или сервер лег - НЕ ПАНИКУЕМ.
            // Просто пишем в лог, что мы работаем автономно.
            e.printStackTrace()
        }

        // Шаг Б: В ЛЮБОМ СЛУЧАЕ отдаем то, что лежит в сейфе (Room)
        // Если обновились - отдадим новое. Если нет - старое. Пустым не останешься.
        return dao.getKebabs().map { it.toDomain() }
    }

    // 2. ВЫДЕРНУТЬ ЧЕЛОВЕКА ПО ID
    override suspend fun getKebabById(id: Int): KebabPlace? {
        // Берем из локальной базы всех и ищем нужного.
        // Можно было бы в DAO написать метод getKebabById(id), но пока и так сойдет.
        return getKebabs().find { it.id == id }
    }

    // 3. ДОБАВИТЬ НОВУЮ ТОЧКУ (СДАТЬ БАРЫГУ)
    override suspend fun addKebab(name: String, description: String, rating: Double, lat: Double, lng: Double) {
        // Лепим DTO для отправки
        val dto = KebabDto(
            id = "0", // Сервер сам разберется с ID
            name = name,
            description = description,
            rating = rating,
            lat = lat,
            lng = lng,
            // Картинка-заглушка (аппетитная)
            image = "https://www.savorythoughts.com/wp-content/uploads/2021/09/Doner-Kebab-Recipe-Savory-Thoughts-8.jpg",
        )

        // Отправляем маляву на сервер
        api.addKebab(dto)

        // ВАЖНЫЙ МОМЕНТ, БРАТ:
        // Сейчас мы просто отправили. Чтобы увидеть новую точку в списке,
        // юзеру надо будет обновить экран (свайпнуть или перезайти),
        // тогда сработает getKebabs() и подтянет обнову.
        // Пока так оставим, для начала пойдет.
    }
}