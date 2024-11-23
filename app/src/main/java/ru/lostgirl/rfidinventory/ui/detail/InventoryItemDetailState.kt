package ru.lostgirl.rfidinventory.ui.detail

import ru.lostgirl.rfidinventory.domain.models.InventoryItemDetailModel

data class InventoryItemDetailState(
    // Список параметров ТМЦ со значениями
    val details: List<InventoryItemDetailModel> = listOf(),
)
