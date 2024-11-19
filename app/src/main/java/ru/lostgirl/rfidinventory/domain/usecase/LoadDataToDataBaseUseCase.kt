package ru.lostgirl.rfidinventory.domain.usecase

import ru.lostgirl.rfidinventory.domain.repository.InventoryRepository
import ru.lostgirl.rfidinventory.utils.LoadDataToDataBaseUtil

// use case Загрузка массива данных в локальную BD
class LoadDataToDataBaseUseCase(
    private val inventoryRepository: InventoryRepository,
) {
    suspend fun execute(data: Array<Array<String>>): Boolean {
        return LoadDataToDataBaseUtil.load(inventoryRepository, data)
    }
}
