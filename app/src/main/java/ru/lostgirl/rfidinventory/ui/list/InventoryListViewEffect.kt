package ru.lostgirl.rfidinventory.ui.list

import ru.lostgirl.rfidinventory.domain.models.InventoryItemState

sealed class InventoryListViewEffect {
    // Показать AlertDialog для подтверждения действия (onOkClickListener) или подсказки
    data class ShowAlertDialog(val message: Int, val onOkClickListener: () -> Unit) :
        InventoryListViewEffect()

    // Показать AlertDialog для настройки
    data class ShowSettingsAlertDialog(
        val itemState: InventoryItemState,
        val onOkClickListener: (resetState: Boolean, setStateFound: Boolean, comment: String) -> Unit,
    ) :
        InventoryListViewEffect()
}
