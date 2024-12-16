package ru.lostgirl.rfidinventory.domain.usecase

import ru.lostgirl.rfidinventory.domain.repository.InventoryRepository

// use case Проверка загружены ли данные по API

class IsDataFromApiUseCase(private val repository: InventoryRepository) {
    fun execute(): Boolean = repository.isDataFromApi()
}