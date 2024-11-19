package ru.lostgirl.rfidinventory.domain.models

// Модель данных ТМЦ для работы в момент сканирования
data class InventoryItemForScanningModel(
    val id: Int?,
    val shipmentNum: String,
    val rfidCardNum: String,
    val actualLocationID: Int?,
)
