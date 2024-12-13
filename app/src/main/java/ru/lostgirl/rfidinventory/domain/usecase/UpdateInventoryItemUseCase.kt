package ru.lostgirl.rfidinventory.domain.usecase

import ru.lostgirl.rfidinventory.domain.repository.InventoryRepository

// use case Изменить актуальное местоположение ТМЦ по id
class UpdateInventoryItemUseCase(private val inventoryRepository: InventoryRepository) {

    fun execute(locationID: Int, location: String, id: Int) {
        return inventoryRepository.updateLocationInventoryItemByID(locationID, location, id)
    }
}
