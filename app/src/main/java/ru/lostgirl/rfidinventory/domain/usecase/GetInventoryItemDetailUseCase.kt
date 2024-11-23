package ru.lostgirl.rfidinventory.domain.usecase

import ru.lostgirl.rfidinventory.domain.models.InventoryItemDetailModel
import ru.lostgirl.rfidinventory.domain.repository.InventoryRepository
import ru.lostgirl.rfidinventory.utils.ResourcesProvider

// use case Получение списка параметров ТМЦ по id для отображения детализации
class GetInventoryItemDetailUseCase(
    private val repository: InventoryRepository,
    private val resourcesProvider: ResourcesProvider,
) {
    fun execute(id: Int, statusColor: Int): List<InventoryItemDetailModel> {
        val itemDetail = repository.getInventoryItemDetail(id)
        return itemDetail.toListOfDetail(resourcesProvider, statusColor)
    }
}
