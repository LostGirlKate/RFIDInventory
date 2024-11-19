package ru.lostgirl.rfidinventory.domain.models

// Модель данных для отображения списка параметров ТМЦ(детализация)
// paraName - название параметра
// value- значение параметра
// isStatus - параметр - статус
// statusColor - цвет статуса
data class InventoryItemDetailModel(
    val paraName: String,
    val value: String,
    val isStatus: Boolean = false,
    val statusColor: Int = 0,
    val isActualLocation: Boolean = false,
)
