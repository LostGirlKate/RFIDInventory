package ru.lostgirl.rfidinventory.data.api.models

// Структура для загрузки данных API

data class InventoryData(
    val locations: List<Location>,
    val items: List<Item>,
)

// Структура для сохранения данных API
data class InventoryDataForSave(
    val items: List<ItemForSave>,
)