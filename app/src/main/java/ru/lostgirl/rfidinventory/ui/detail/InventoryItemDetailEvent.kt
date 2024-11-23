package ru.lostgirl.rfidinventory.ui.detail

import ru.lostgirl.rfidinventory.domain.models.InventoryItemState

sealed class InventoryItemDetailEvent {
    // Открытие детализации ТМЦ (параметры id ТМЦ и текущее состояние ТМЦ)
    data class OpenDetails(val itemId: Int, val state: InventoryItemState) :
        InventoryItemDetailEvent()
}
