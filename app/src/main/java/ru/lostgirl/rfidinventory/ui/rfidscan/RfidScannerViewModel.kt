package ru.lostgirl.rfidinventory.ui.rfidscan

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.lostgirl.rfidinventory.R
import ru.lostgirl.rfidinventory.data.readers.ReaderType
import ru.lostgirl.rfidinventory.domain.models.InventoryItemForListModel
import ru.lostgirl.rfidinventory.domain.models.InventoryItemState
import ru.lostgirl.rfidinventory.domain.usecase.barcodereader.CloseBarcodeReaderUseCase
import ru.lostgirl.rfidinventory.domain.usecase.GetAllInventoryItemListForScanningUseCase
import ru.lostgirl.rfidinventory.domain.usecase.GetInventoryInfoUseCase
import ru.lostgirl.rfidinventory.domain.usecase.GetInventoryItemByLocationIDUseCase
import ru.lostgirl.rfidinventory.domain.usecase.refidreader.GetRFIDReaderPowerUseCase
import ru.lostgirl.rfidinventory.domain.usecase.refidreader.IsRFIDReaderInitializedUseCase
import ru.lostgirl.rfidinventory.domain.usecase.barcodereader.OpenBarcodeReaderUseCase
import ru.lostgirl.rfidinventory.domain.usecase.ResetLocationInventoryItemByIDUseCase
import ru.lostgirl.rfidinventory.domain.usecase.SetCommentInventoryItemByIDUseCase
import ru.lostgirl.rfidinventory.domain.usecase.SetFoundInventoryItemByIDUseCase
import ru.lostgirl.rfidinventory.domain.usecase.barcodereader.StartBarcodeReaderUseCase
import ru.lostgirl.rfidinventory.domain.usecase.refidreader.StartRFiDInventoryUseCase
import ru.lostgirl.rfidinventory.domain.usecase.barcodereader.StopBarcodeReaderUseCase
import ru.lostgirl.rfidinventory.domain.usecase.refidreader.StopRFIDInventoryUseCase
import ru.lostgirl.rfidinventory.domain.usecase.UpdateInventoryItemUseCase
import ru.lostgirl.rfidinventory.mvi.MviViewModel

class RfidScannerViewModel(
    private val updateInventoryItemUseCase: UpdateInventoryItemUseCase,
    private val getInventoryItemByLocationIDUseCase: GetInventoryItemByLocationIDUseCase,
    private val getInventoryInfoUseCase: GetInventoryInfoUseCase,
    private val startRFiDInventoryUseCase: StartRFiDInventoryUseCase,
    private val stopRFiDInventoryUseCase: StopRFIDInventoryUseCase,
    private val getRFIDReaderPowerUseCase: GetRFIDReaderPowerUseCase,
    private val isRFIDReaderInitializedUseCase: IsRFIDReaderInitializedUseCase,
    private val openBarcodeReaderUseCase: OpenBarcodeReaderUseCase,
    private val closeBarcodeReaderUseCase: CloseBarcodeReaderUseCase,
    private val startBarcodeReaderUseCase: StartBarcodeReaderUseCase,
    private val stopBarcodeReaderUseCase: StopBarcodeReaderUseCase,
    private val resetLocationInventoryItemByID: ResetLocationInventoryItemByIDUseCase,
    private val getAllInventoryItemListForRfidScanningUseCase: GetAllInventoryItemListForScanningUseCase,
    private val setFoundInventoryItemByIDUseCase: SetFoundInventoryItemByIDUseCase,
    private val setCommentInventoryItemByIDUseCase: SetCommentInventoryItemByIDUseCase,
    application: Application,

    ) :
    MviViewModel<RfidScannerViewState, RfidScannerViewEffect, RfidScannerViewEvent>(application),
    DefaultLifecycleObserver {

    init {
        viewState =
            RfidScannerViewState(
                inventoryItemsFullRFIDList = getAllInventoryItemListForRfidScanningUseCase.execute(),
                scannerPowerValue = 0
            )
    }

    override fun process(viewEvent: RfidScannerViewEvent) {
        super.process(viewEvent)
        when (viewEvent) {
            is RfidScannerViewEvent.SetCurrentLocation -> {
                setCurrentLocation(
                    viewEvent.iLocation,
                    viewEvent.locationName
                )
            }

            is RfidScannerViewEvent.SetScanningState -> {
                setScanningState(viewEvent.state)
            }

            is RfidScannerViewEvent.SetScanningPower -> {
                setScannerPowerValue(viewEvent.power)
            }

            is RfidScannerViewEvent.SetScannerType -> {
                setScannerType(viewEvent.type)
            }

            is RfidScannerViewEvent.ShowAlertDialog -> {
                showAlertDialog(
                    viewEvent.message,
                    viewEvent.onOkClickListener
                )
            }

            is RfidScannerViewEvent.ResetInventoryItemState -> {
                resetLocationInventory(viewEvent.item)
            }

            is RfidScannerViewEvent.SetCommentInventoryItem -> {
                setCommentInventoryItem(viewEvent.item, viewEvent.comment)
            }

            is RfidScannerViewEvent.SetFoundInventoryItemState -> {
                setFoundInventoryItemState(viewEvent.item)
            }

            is RfidScannerViewEvent.ShowSettingsAlertDialog -> {
                showSettingsAlertDialog(
                    viewEvent.itemState,
                    viewEvent.onOkClickListener
                )
            }
        }
    }

    // При сворачивании окна останавливаем считывание если оно было запущено
    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        if (viewState.isScanningStart) {
            setScanningState(false)
            stopInventory()
        }
    }


    private fun showAlertDialog(message: Int, onOkClickListener: () -> Unit) {
        if (!viewState.isScanningStart) {
            viewEffect = RfidScannerViewEffect.ShowAlertDialog(message, onOkClickListener)
        }
    }

    private fun showSettingsAlertDialog(
        itemState: InventoryItemState,
        onOkClickListener: (resetState: Boolean, setStateFound: Boolean, comment: String) -> Unit,
    ) {
        if (!viewState.isScanningStart) {
            viewEffect = RfidScannerViewEffect.ShowSettingsAlertDialog(itemState, onOkClickListener)
        }
    }

    // Установка типа считывателя
    private fun setScannerType(type: ReaderType) {
        initReader(type)
    }

    // Старт инвентаризации (для RFID начинаем считывание, для 2D начинаем сканирование)
    private fun startInventory() {
        when (viewState.scannerType) {
            ReaderType.RFID -> {
                // получаем мощность из настроек
                val power = viewState.scannerPowerValue
                // стартуем считывание, при успешном - запускаем проверку метки на наличие в списке ТМЦ
                startRFiDInventoryUseCase.execute(power, onError = this::onError) {
                    for (tag in it) {
                        checkItemNewLocationAndUpdate(tag)
                    }
                }
            }

            ReaderType.BARCODE_2D -> {
                startBarcodeReaderUseCase.execute()
            }

            else -> {
                onError()
            }
        }
    }

    // Ошибки инициализации считывателя
    private fun onError(message: String = "") {
        viewEffect = RfidScannerViewEffect.ShowToast(
            R.string.init_rfid_message, R.string.init_error_rfid_message,
            true
        )
    }

    // Останавливаем инвентаризацию (для RFID останавливаем считывание, для 2D останавливаем сканирование)
    private fun stopInventory() {
        when (viewState.scannerType) {
            ReaderType.RFID -> {
                stopRFiDInventoryUseCase.execute()
            }

            ReaderType.BARCODE_2D -> {
                stopBarcodeReaderUseCase.execute()
            }

            else -> {
                onError()
            }
        }
    }

    // инициализация считывателя
    private fun initReader(type: ReaderType) {
        viewModelScope.launch {
            val result = when (type) {
                ReaderType.RFID -> {
                    // проверяем инициализирован ли счытыватель
                    isRFIDReaderInitializedUseCase.execute()
                }

                ReaderType.BARCODE_2D -> {
                    withContext(Dispatchers.IO) {
                        // инициализируем 2D сканер с callback для обработки результата сканировния
                        openBarcodeReaderUseCase.execute { checkItemNewLocationAndUpdate(it) }
                    }
                }
            }
            // если RFID счытыватель инициализирован - получем его мощность
            var power = if (result && type == ReaderType.RFID) {
                getRFIDReaderPowerUseCase.execute()
            } else 0
            if (power < 0 && type == ReaderType.RFID) {
                power = -1
                onError()
            }
            // обновляем состояние окна по результату инициализации
            viewState = viewState.copy(
                scannerType = type,
                isReaderInit = result,
                panelSettingsVisible = (result && type == ReaderType.RFID),
                startScanningButtonVisible = (result && type == ReaderType.RFID),
                scannerPowerValue = power
            )
        }
    }

    // Установка текущего местоположения
    private fun setCurrentLocation(locationId: Int, locationName: String) {
        viewState = viewState.copy(
            currentLocation = locationId,
            currentLocationName = locationName,
            inventoryState = getInventoryInfoUseCase.execute(locationId),
            inventoryItems = getInventoryItemByLocationIDUseCase.execute(
                locationId
            )
        )
    }

    // Установка состояние сканирования
    private fun setScanningState(state: Boolean) {
        viewState = viewState.copy(
            canBackPress = !state,
            isScanningStart = state,
            startScanningButtonVisible = (!state && viewState.scannerType == ReaderType.RFID),
            panelSettingsVisible = (!state && viewState.scannerType == ReaderType.RFID),
            stopScanningButtonVisible = (state && viewState.scannerType == ReaderType.RFID)
        )
        if (state) {
            startInventory()
        } else {
            stopInventory()
        }
    }

    // Обновления мощности при ручной настройке
    private fun setScannerPowerValue(value: Int) {
        viewState = viewState.copy(
            scannerPowerValue = value
        )
    }

    // Обновление актуального местоположения ТМЦ
    private fun updateInventoryItem(locationID: Int, location: String, id: Int) {
        updateInventoryItemUseCase.execute(locationID, location, id)
        viewState = viewState.copy(
            inventoryState = getInventoryInfoUseCase.execute(viewState.currentLocation),
            inventoryItems = getInventoryItemByLocationIDUseCase.execute(
                viewState.currentLocation
            ),
            inventoryItemsFullRFIDList = getAllInventoryItemListForRfidScanningUseCase.execute(),
        )
    }


    // Завершение  нвентаризации (сохранение данных в файл и при успешном сохранении очистка BD)
    private fun resetLocationInventory(item: InventoryItemForListModel) {
        viewModelScope.launch(Dispatchers.IO) {
            item.id?.let { resetLocationInventoryById(it) }
        }
    }

    private fun resetLocationInventoryById(id: Int) {
        resetLocationInventoryItemByID.execute(id)
        viewState = viewState.copy(
            inventoryState = getInventoryInfoUseCase.execute(viewState.currentLocation),
            inventoryItems = getInventoryItemByLocationIDUseCase.execute(
                viewState.currentLocation
            ),
            inventoryItemsFullRFIDList = getAllInventoryItemListForRfidScanningUseCase.execute(),
        )
    }

    // установка статуса найдено вручную
    private fun setFoundInventoryItemState(item: InventoryItemForListModel) {
        viewModelScope.launch(Dispatchers.IO) {
            item.id?.let { setFoundInventoryItemByID(it) }
        }
    }

    // установка примечания
    private fun setCommentInventoryItem(item: InventoryItemForListModel, comment: String) {
        viewModelScope.launch(Dispatchers.IO) {
            item.id?.let { setCommentInventoryItemByID(it, comment) }
        }
    }

    // установка статуса найдено вручную
    private fun setFoundInventoryItemByID(id: Int) {
        setFoundInventoryItemByIDUseCase.execute(id)
        viewState = viewState.copy(
            inventoryState = getInventoryInfoUseCase.execute(viewState.currentLocation),
            inventoryItems = getInventoryItemByLocationIDUseCase.execute(
                viewState.currentLocation
            ),
            inventoryItemsFullRFIDList = getAllInventoryItemListForRfidScanningUseCase.execute(),
        )
    }

    // установка комментария
    private fun setCommentInventoryItemByID(id: Int, comment: String) {
        setCommentInventoryItemByIDUseCase.execute(id, comment)
    }


    // Проверка наличия метки в списке ТМЦ
    private fun checkItemNewLocationAndUpdate(scanCode: String) {
        val allItem = viewState.inventoryItemsFullRFIDList
        //ищем id ТМЦ (RFID по rfidCardNum, 2D по shipmentNum)
        val itemId =
            when (viewState.scannerType) {
                ReaderType.RFID -> {
                    allItem.firstOrNull { it.rfidCardNum == scanCode && it.actualLocationID != viewState.currentLocation }?.id
                        ?: 0
                }

                ReaderType.BARCODE_2D -> {
                    allItem.firstOrNull { it.shipmentNum == scanCode && it.actualLocationID != viewState.currentLocation }?.id
                        ?: 0
                }

                else -> {
                    0
                }
            }
        // если метка найдена обновляем актальное местоположение у ТМЦ и проигрываем звук
        if (itemId > 0) {
            viewEffect = RfidScannerViewEffect.PlaySound(1)
            updateInventoryItem(
                viewState.currentLocation,
                viewState.currentLocationName,
                itemId
            )
            // после обновления проверяем текущее состояние инвентаризации
            // (если 100% найдено, тогда сообщаем - Все метки найдены)
            if (viewState.inventoryState.percentFound == 100) {
                viewEffect = RfidScannerViewEffect.InventoryReady(R.string.all_tag_found)
            }
        }
        // если активен 2D сканер - останавливаем его после каждого успешного сканирования
        if (viewState.scannerType == ReaderType.BARCODE_2D) {
            setScanningState(false)
        }
    }
}
