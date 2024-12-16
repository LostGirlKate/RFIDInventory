package ru.lostgirl.rfidinventory.ui.main

import android.content.ContentResolver
import android.net.Uri
import androidx.appcompat.app.AlertDialog

sealed class InventoryMainViewEvent {
    // Загрузка данных из файла
    data class LoadDataFromFile(
        val uri: Uri,
        val contentResolver: ContentResolver,
        val processDialog: AlertDialog? = null,
    ) : InventoryMainViewEvent()

    // Загрузка данных с сервера
    data class LoadDataFromApi(
        val processDialog: AlertDialog? = null,
    ) : InventoryMainViewEvent()

    // Открыть окно для выбора файла для загрузки
    data object OpenFileManager : InventoryMainViewEvent()

    // Обновить даннные о состоянии инвентаризации
    data object RefreshData : InventoryMainViewEvent()

    // Показать ProcessDialog при выполнении долгих расчетов, в параметр передаем Event,
    // который запустится после начала отображения ProcessDialog
    data class ShowProcessDialog(val message: Int, val processEvent: InventoryMainViewEvent) :
        InventoryMainViewEvent()

    // Сохранить данные в файл
    data class SaveDataToFile(
        val processDialog: AlertDialog? = null,
    ) : InventoryMainViewEvent()

    // Сохранить данные на сервер
    data class SaveDataToApi(
        val processDialog: AlertDialog? = null,
    ) : InventoryMainViewEvent()

    // Завершить инвентаризацию (выгрузить данные в Excel и очистить BD)
    data class CloseInventory(val processDialog: AlertDialog? = null) : InventoryMainViewEvent()

    // Показать AlertDialog для подтверждения действия (onOkClickListener) или подсказки
    data class ShowAlertDialog(val message: Int, val onOkClickListener: () -> Unit) :
        InventoryMainViewEvent()
}
