package ru.lostgirl.rfidinventory.ui.list

import ru.lostgirl.rfidinventory.domain.models.InventoryItemForListModel
import ru.lostgirl.rfidinventory.domain.models.InventoryItemState

sealed class InventoryListViewEvent {
    // Изменение фильтра по местоположению
    data class EditCurrentLocation(val locationID: Int) : InventoryListViewEvent()

    // Показать AlertDialog для подтверждения действия (onOkClickListener) или подсказки
    data class ShowAlertDialog(val message: Int, val onOkClickListener: () -> Unit) :
        InventoryListViewEvent()

    // Показать AlertDialog для настройки
    data class ShowSettingsAlertDialog(
        val itemState: InventoryItemState,
        val onOkClickListener: (resetState: Boolean, setStateFound: Boolean, comment: String) -> Unit,
    ) :
        InventoryListViewEvent()

    // отмена текуущего статуса
    data class ResetInventoryItemState(val item: InventoryItemForListModel) :
        InventoryListViewEvent()

    // установка статуса найдено вручную
    data class SetFoundInventoryItemState(val item: InventoryItemForListModel) :
        InventoryListViewEvent()


    // установка примечания
    data class SetCommentInventoryItem(val item: InventoryItemForListModel, val comment: String) :
        InventoryListViewEvent()
}
