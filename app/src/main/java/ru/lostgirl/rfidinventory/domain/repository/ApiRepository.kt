package ru.lostgirl.rfidinventory.domain.repository

import ru.lostgirl.rfidinventory.data.api.models.ItemForSave

interface ApiRepository {

    // Загрузка данных по API
    suspend fun load()

    // Сохранение данных по API
    suspend fun saveItems()
}