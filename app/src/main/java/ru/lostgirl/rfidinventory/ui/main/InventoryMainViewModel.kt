package ru.lostgirl.rfidinventory.ui.main

import android.app.Application
import android.content.ContentResolver
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.lostgirl.rfidinventory.R
import ru.lostgirl.rfidinventory.domain.usecase.ClearDataBaseUseCase
import ru.lostgirl.rfidinventory.domain.usecase.GetInventoryInfoUseCase
import ru.lostgirl.rfidinventory.domain.usecase.IsDataFromApiUseCase
import ru.lostgirl.rfidinventory.domain.usecase.LoadDataToDataBaseUseCase
import ru.lostgirl.rfidinventory.domain.usecase.api.ExportDataToApiUseCase
import ru.lostgirl.rfidinventory.domain.usecase.api.LoadDataFromApiUseCase
import ru.lostgirl.rfidinventory.domain.usecase.excel.ExportDataToExcelFileUseCase
import ru.lostgirl.rfidinventory.domain.usecase.excel.GetDataForExcelUseCase
import ru.lostgirl.rfidinventory.domain.usecase.excel.GetDataFromExcelUseCase
import ru.lostgirl.rfidinventory.domain.usecase.refidreader.IsRFIDReaderInitializedUseCase
import ru.lostgirl.rfidinventory.mvi.MviViewModel
import ru.lostgirl.rfidinventory.utils.error.AppError
import java.text.SimpleDateFormat
import java.util.Locale

class InventoryMainViewModel(
    private val getInventoryInfo: GetInventoryInfoUseCase,
    private val loadDataToDataBaseUseCase: LoadDataToDataBaseUseCase,
    private val getDataForExcelUseCase: GetDataForExcelUseCase,
    private val clearDataBaseUseCase: ClearDataBaseUseCase,
    private val getDataFromExcelUseCase: GetDataFromExcelUseCase,
    private val exportDataToExcelFileUseCase: ExportDataToExcelFileUseCase,
    private val isRFIDReaderInitializedUseCase: IsRFIDReaderInitializedUseCase,
    private val loadDataFromApiUseCase: LoadDataFromApiUseCase,
    private val exportDataToApiUseCase: ExportDataToApiUseCase,
    private val isDataFromApiUseCase: IsDataFromApiUseCase,
    application: Application,
) :
    MviViewModel<InventoryMainViewState, InventoryMainViewEffect, InventoryMainViewEvent>(
        application
    ) {

    init {
        viewState = InventoryMainViewState(getInventoryInfo.execute(), isDataFromApiUseCase.execute())
    }

    override fun process(viewEvent: InventoryMainViewEvent) {
        super.process(viewEvent)
        when (viewEvent) {
            InventoryMainViewEvent.OpenFileManager -> {
                openFileManager()
            }

            is InventoryMainViewEvent.LoadDataFromFile -> {
                loadDataFromExcel(
                    viewEvent.uri,
                    viewEvent.contentResolver,
                    viewEvent.processDialog
                )
            }

            is InventoryMainViewEvent.SaveDataToFile -> {
                exportDataToExcel(
                    viewEvent.processDialog
                )
            }

            is InventoryMainViewEvent.CloseInventory -> {
                closeInventory(viewEvent.processDialog)
            }

            is InventoryMainViewEvent.ShowProcessDialog -> {
                showProcessDialog(
                    viewEvent.message,
                    viewEvent.processEvent
                )
            }

            is InventoryMainViewEvent.ShowAlertDialog -> {
                showAlertDialog(
                    viewEvent.message,
                    viewEvent.onOkClickListener
                )
            }

            InventoryMainViewEvent.RefreshData -> {
                refreshInventoryState()
            }

            is InventoryMainViewEvent.LoadDataFromApi -> {
                loadDataFromApi(viewEvent.processDialog)
            }

            is InventoryMainViewEvent.SaveDataToApi -> {
                exportDataToApi(viewEvent.processDialog)
            }
        }
    }

    private fun openFileManager() {
        viewEffect = InventoryMainViewEffect.OpenFileManager
    }

    private fun showProcessDialog(message: Int, processEvent: InventoryMainViewEvent) {
        viewEffect = InventoryMainViewEffect.ShowAlertProcessDialog(message, processEvent)
    }

    private fun showAlertDialog(message: Int, onOkClickListener: () -> Unit) {
        viewEffect = InventoryMainViewEffect.ShowAlertDialog(message, onOkClickListener)
    }

    // Загрузка данных из файла и сохранение их в BD
    private fun loadDataFromExcel(
        uri: Uri,
        contentResolver: ContentResolver,
        processDialog: AlertDialog?,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val dataArray =
                getDataFromExcelUseCase.execute(
                    uri,
                    contentResolver
                )
            val parseResult = parseDataToDataBase(dataArray) && dataArray.isNotEmpty()
            viewEffect = InventoryMainViewEffect.HideAlertProcessDialog(processDialog)
            viewEffect = InventoryMainViewEffect.ShowToast(
                R.string.data_load_message, R.string.data_error_load_message,
                !parseResult
            )
        }
    }

    // Загрузка данных с сервера и сохранение их в BD
    private fun loadDataFromApi(
        processDialog: AlertDialog?,
    ) {
        var parseResult = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                loadDataFromApiUseCase.execute()
                viewState = viewState.copy(
                    inventoryState = getInventoryInfo.execute(),
                    isDataFromApi = isDataFromApiUseCase.execute()
                )
            } catch (e: AppError) {
                parseResult = false
            }

            viewEffect = InventoryMainViewEffect.HideAlertProcessDialog(processDialog)
            viewEffect = InventoryMainViewEffect.ShowToast(
                R.string.data_load_message, R.string.data_error_load_message,
                !parseResult
            )
        }
    }

    // Сохранение данных на сервер
    private fun exportDataToApi(
        processDialog: AlertDialog?,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val loadResult = saveDataToApi()
            viewEffect = InventoryMainViewEffect.HideAlertProcessDialog(processDialog)
            viewEffect = InventoryMainViewEffect.ShowToast(
                R.string.api_save_message, R.string.api_error_save_message,
                !loadResult
            )
        }
    }

    // Экспорт данных в Excel файл
    private fun exportDataToExcel(
        processDialog: AlertDialog?,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val loadResult = exportData()
            viewEffect = InventoryMainViewEffect.HideAlertProcessDialog(processDialog)
            viewEffect = InventoryMainViewEffect.ShowToast(
                R.string.file_save_message, R.string.file_error_save_message,
                !loadResult
            )
        }
    }

    // Обновление информации об инвентаризации
    private fun refreshInventoryState() {
        viewState =
            viewState.copy(inventoryState = getInventoryInfo.execute(), isDataFromApi = isDataFromApiUseCase.execute())
    }

    // Завершение  нвентаризации (сохранение данных в файл и при успешном сохранении очистка BD)
    private fun closeInventory(processDialog: AlertDialog?) {
        viewModelScope.launch(Dispatchers.IO) {
            var result = if (viewState.isDataFromApi) saveDataToApi() else exportData()
            if (result) {
                result = clearDataBaseUseCase.execute()
            }
            refreshInventoryState()
            viewEffect = InventoryMainViewEffect.HideAlertProcessDialog(processDialog)
            viewEffect = InventoryMainViewEffect.ShowToast(
                R.string.data_save_message, R.string.data_error_save_message,
                !result
            )
        }
    }

    // Экспорт данных в Excel
    private suspend fun exportData(): Boolean {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH.mm", Locale.US)
        val fileName = "inventory_${dateFormat.format(System.currentTimeMillis())}.xlsx"
        val data = getDataForExcelUseCase.execute()
        return exportDataToExcelFileUseCase.execute(
            data = data,
            fileName = fileName
        )
    }

    // Сохранение данных по API
    private suspend fun saveDataToApi(): Boolean {
        var result = true
        try {
            exportDataToApiUseCase.execute()
        } catch (e: AppError) {
            result = false
        }
        return result
    }

    // Сохранение массива данных в BD
    private suspend fun parseDataToDataBase(data: Array<Array<String>>): Boolean {
        val result = loadDataToDataBaseUseCase.execute(data)
        viewState = viewState.copy(
            inventoryState = getInventoryInfo.execute(),
            isDataFromApi = isDataFromApiUseCase.execute()
        )
        return result
    }
}
