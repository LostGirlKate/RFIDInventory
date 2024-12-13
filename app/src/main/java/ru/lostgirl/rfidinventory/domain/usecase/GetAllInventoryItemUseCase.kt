package ru.lostgirl.rfidinventory.domain.usecase

import ru.lostgirl.rfidinventory.domain.models.InventoryItemFullModel
import ru.lostgirl.rfidinventory.domain.repository.InventoryRepository

// use case Получение общего списка ТМЦ
class GetAllInventoryItemUseCase(private val inventoryRepository: InventoryRepository) {

    fun execute(): List<InventoryItemFullModel> {
        return inventoryRepository.getAllInventoryFullList()
    }
}
