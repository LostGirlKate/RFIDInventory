package ru.lostgirl.rfidinventory.ui.main

import androidx.appcompat.app.AlertDialog

sealed class InventoryMainViewEffect {
    // Показать Toast (с возможностью настройки isError)
    data class ShowToast(val message: Int, val errorMessage: Int, val isError: Boolean = false) :
        InventoryMainViewEffect()

    // Показать ProcessDialog при выполнении долгих расчетов, в параметр передаем Event,
    // который запустится после начала отображения ProcessDialog
    data class ShowAlertProcessDialog(val message: Int, val processEvent: InventoryMainViewEvent) :
        InventoryMainViewEffect()

    // Закрыть ProcessDialog после выполнения расчетов
    data class HideAlertProcessDialog(val processDialog: AlertDialog?) : InventoryMainViewEffect()

    // Показать AlertDialog для подтверждения действия (onOkClickListener) или подсказки
    data class ShowAlertDialog(val message: Int, val onOkClickListener: () -> Unit) :
        InventoryMainViewEffect()

    // Открыть окно выбора файл для загрузки
    object OpenFileManager : InventoryMainViewEffect()
}
