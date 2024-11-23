package ru.lostgirl.rfidinventory.domain.usecase

import ru.lostgirl.rfidinventory.domain.repository.InventoryRepository

// use case reset актуального местоположения ТМЦ по id
class ResetLocationInventoryItemByIDUseCase(private val repository: InventoryRepository) {
    fun execute(id: Int) {
        return repository.resetLocationInventoryItemByID(id)
    }
}
