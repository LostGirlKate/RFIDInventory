package ru.lostgirl.rfidinventory.domain.usecase

import ru.lostgirl.rfidinventory.domain.models.InventoryItemForListModel
import ru.lostgirl.rfidinventory.domain.repository.InventoryRepository

// use case Получение списка ТМЦ по местоположению
class GetInventoryItemByLocationIDUseCase(private val inventoryRepository: InventoryRepository) {

    fun execute(locationID: Int): List<InventoryItemForListModel> {
        return inventoryRepository.getInventoryItemByLocationID(locationID)
            .sortedBy { it.inventoryNum }
    }
}
