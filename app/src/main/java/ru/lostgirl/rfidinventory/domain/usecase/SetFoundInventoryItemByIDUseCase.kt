package ru.lostgirl.rfidinventory.domain.usecase

import ru.lostgirl.rfidinventory.domain.repository.InventoryRepository

// use case Установить статус найдено вручную
class SetFoundInventoryItemByIDUseCase(private val repository: InventoryRepository) {
    fun execute(id: Int) {
        return repository.setFoundInventoryItemByID(id)
    }
}
