package ru.lostgirl.rfidinventory.ui.rfidscan

import ru.lostgirl.rfidinventory.data.readers.ReaderType
import ru.lostgirl.rfidinventory.domain.models.InventoryInfoModel
import ru.lostgirl.rfidinventory.domain.models.InventoryItemForListModel
import ru.lostgirl.rfidinventory.domain.models.InventoryItemForScanningModel

data class RfidScannerViewState(
    // Сканирование активно
    val isScanningStart: Boolean = false,
    // RFID ридер инициализирован
    val isReaderInit: Boolean = false,
    // Есть возможность вернуться назад
    val canBackPress: Boolean = true,
    // Кнопка начала сканирования видна
    val startScanningButtonVisible: Boolean = false,
    // Видна панель настроек RFID ридера
    val panelSettingsVisible: Boolean = false,
    // Кнопка завершения сканирования видна
    val stopScanningButtonVisible: Boolean = false,
    // Значение мощности RFID сканера
    val scannerPowerValue: Int = 50,
    // Текущее местоположение
    val currentLocation: Int = 0,
    // Имя текущего местоположения
    val currentLocationName: String = "",
    // Список ТМЦ для отображения списка
    val inventoryItems: List<InventoryItemForListModel> = listOf(),
    // Состояние инвентаризации
    val inventoryState: InventoryInfoModel = InventoryInfoModel(),
    // Список меток для контроля при сканировании
    val inventoryItemsFullRFIDList: List<InventoryItemForScanningModel> = listOf(),
    // Тип считывателя
    val scannerType: ReaderType? = null,
)
