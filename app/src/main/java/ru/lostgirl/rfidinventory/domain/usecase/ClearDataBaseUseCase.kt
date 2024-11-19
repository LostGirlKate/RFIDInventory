package ru.lostgirl.rfidinventory.domain.usecase

import ru.lostgirl.rfidinventory.data.storage.roomdb.MainDataBase
import ru.lostgirl.rfidinventory.domain.repository.InventoryRepository

// use case Очистка DB
class ClearDataBaseUseCase(
    private val inventoryRepository: InventoryRepository,
    private val dataBase: MainDataBase,
) {
    suspend fun execute(): Boolean {
        return inventoryRepository.clearAll(dataBase)
    }
}
