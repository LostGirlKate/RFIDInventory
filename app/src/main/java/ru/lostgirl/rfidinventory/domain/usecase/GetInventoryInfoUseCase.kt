package ru.lostgirl.rfidinventory.domain.usecase

import ru.lostgirl.rfidinventory.domain.models.InventoryInfoModel
import ru.lostgirl.rfidinventory.domain.repository.InventoryRepository

// use case Получение информации об инвентаризации
class GetInventoryInfoUseCase(private val inventoryRepository: InventoryRepository) {
    fun execute(locationID: Int = -1): InventoryInfoModel {
        return inventoryRepository.getInventoryItemsCounts(locationID = locationID)
    }
}
