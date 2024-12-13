package ru.lostgirl.rfidinventory.domain.usecase

import ru.lostgirl.rfidinventory.domain.models.InventoryItemForScanningModel
import ru.lostgirl.rfidinventory.domain.repository.InventoryRepository

// use case Получение общего списка меток для сканирования
class GetAllInventoryItemListForScanningUseCase(private val inventoryRepository: InventoryRepository) {

    fun execute(): List<InventoryItemForScanningModel> {
        return inventoryRepository.getAllInventoryItemListForRfidScanning()
    }
}
