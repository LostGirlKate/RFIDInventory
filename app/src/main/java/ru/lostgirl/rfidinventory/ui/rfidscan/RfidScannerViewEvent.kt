package ru.lostgirl.rfidinventory.ui.rfidscan

import ru.lostgirl.rfidinventory.data.readers.ReaderType
import ru.lostgirl.rfidinventory.domain.models.InventoryItemForListModel
import ru.lostgirl.rfidinventory.domain.models.InventoryItemState

sealed class RfidScannerViewEvent {

    // Установить текущее местоположение
    data class SetCurrentLocation(val iLocation: Int, val locationName: String) :
        RfidScannerViewEvent()

    // Установить статус скнирования
    data class SetScanningState(val state: Boolean) : RfidScannerViewEvent()

    // Установить мощность считывателя
    data class SetScanningPower(val power: Int) : RfidScannerViewEvent()

    // Установить тип считывателя
    data class SetScannerType(val type: ReaderType) : RfidScannerViewEvent()

    // Показать AlertDialog для подтверждения действия (onOkClickListener) или подсказки
    data class ShowAlertDialog(val message: Int, val onOkClickListener: () -> Unit) :
        RfidScannerViewEvent()

    // отмена текуущего статуса
    data class ResetInventoryItemState(val item: InventoryItemForListModel) :
        RfidScannerViewEvent()

    // Показать AlertDialog для настройки
    data class ShowSettingsAlertDialog(
        val itemState: InventoryItemState,
        val onOkClickListener: (resetState: Boolean, setStateFound: Boolean, comment: String) -> Unit,
    ) : RfidScannerViewEvent()

    // установка статуса найдено вручную
    data class SetFoundInventoryItemState(val item: InventoryItemForListModel) :
        RfidScannerViewEvent()

    // установка примечания
    data class SetCommentInventoryItem(val item: InventoryItemForListModel, val comment: String) :
        RfidScannerViewEvent()
}
