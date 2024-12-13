package ru.lostgirl.rfidinventory.domain.usecase.excel

import ru.lostgirl.rfidinventory.domain.repository.InventoryRepository

// use case Получение данных для выгрузки в Excel
class GetDataForExcelUseCase(private val inventoryRepository: InventoryRepository) {
    fun execute(): List<List<String>> {
        return inventoryRepository.getAllInventoryItemForExport().map { it.toListOfString() }
    }
}
