package ru.lostgirl.rfidinventory.domain.repository

import ru.lostgirl.rfidinventory.data.api.models.Item

interface ApiRepository {
    suspend fun load()
    suspend fun saveItems(items: List<Item>)
}