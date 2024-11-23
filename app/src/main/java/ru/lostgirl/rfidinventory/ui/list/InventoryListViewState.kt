package ru.lostgirl.rfidinventory.ui.list

import ru.lostgirl.rfidinventory.domain.models.InventoryInfoModel
import ru.lostgirl.rfidinventory.domain.models.InventoryItemForListModel
import ru.lostgirl.rfidinventory.domain.models.InventoryLocationFullModel

data class InventoryListViewState(
    // Список местоположений
    val locations: List<InventoryLocationFullModel> = listOf(),
    // Список ТМЦ
    val inventoryItems: List<InventoryItemForListModel> = listOf(),
    // Состояние инвентаризации
    val inventoryState: InventoryInfoModel = InventoryInfoModel(),
    // Фильтр по местоположению - актуальное значение
    val currentLocationID: Int = -1,
)
