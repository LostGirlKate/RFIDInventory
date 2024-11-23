package ru.lostgirl.rfidinventory.domain.models


// Модель данных ТМЦ для показа в списке в окне сканирования и просмотра инвентаризации
data class InventoryItemForListModel(
    val id: Int?,
    val model: String,
    val state: InventoryItemState,
    val location: String,
    val oldLocation: String,
    val inventoryNum: String,
    val managerName: String,
    val rfidCardNum: String,
)
