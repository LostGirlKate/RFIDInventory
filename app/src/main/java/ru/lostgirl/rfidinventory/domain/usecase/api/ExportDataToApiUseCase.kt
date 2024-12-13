package ru.lostgirl.rfidinventory.domain.usecase.api

import ru.lostgirl.rfidinventory.domain.models.InventoryItemFullModel
import ru.lostgirl.rfidinventory.domain.repository.ApiRepository

// use case Выгрузка данных по API
class ExportDataToApiUseCase(private val apiRepository: ApiRepository) {
    suspend fun execute(list: List<InventoryItemFullModel>) {
        apiRepository.saveItems(list.map { it.toApiItem() })
    }
}