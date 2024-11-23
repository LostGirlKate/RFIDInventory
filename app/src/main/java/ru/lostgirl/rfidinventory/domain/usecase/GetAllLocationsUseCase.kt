package ru.lostgirl.rfidinventory.domain.usecase

import ru.lostgirl.rfidinventory.domain.models.InventoryLocationFullModel
import ru.lostgirl.rfidinventory.domain.repository.InventoryRepository

// use case Получение общего списка местоположений
class GetAllLocationsUseCase(private val inventoryRepository: InventoryRepository) {

    fun execute(): List<InventoryLocationFullModel> {
        return inventoryRepository.getAllLocationList().sortedBy { it.name }
    }
}
