package ru.lostgirl.rfidinventory.domain.repository

import ru.lostgirl.rfidinventory.data.storage.roomdb.MainDataBase
import ru.lostgirl.rfidinventory.domain.models.InventoryInfoModel
import ru.lostgirl.rfidinventory.domain.models.InventoryItemForDetailFullModel
import ru.lostgirl.rfidinventory.domain.models.InventoryItemForExportModel
import ru.lostgirl.rfidinventory.domain.models.InventoryItemForListModel
import ru.lostgirl.rfidinventory.domain.models.InventoryItemForScanningModel
import ru.lostgirl.rfidinventory.domain.models.InventoryItemFullModel
import ru.lostgirl.rfidinventory.domain.models.InventoryLocationFullModel

interface InventoryRepository {

    // insert ТМЦ
    suspend fun insertInventoryLocation(location: InventoryLocationFullModel): Boolean

    // общий список местопожений (справочник)
    fun getAllLocationList(): List<InventoryLocationFullModel>

    // общий список ТМЦ
    fun getAllInventoryFullList(): List<InventoryItemFullModel>

    // insert ТМЦ list
    fun insertManyInventoryItem(items: List<InventoryItemFullModel>): List<Long>

    // список ТМЦ по местоположению
    fun getInventoryItemByLocationID(locationID: Int): List<InventoryItemForListModel>

    // Информация об общем количестве объектов для инвентаризации
    fun getInventoryItemsCounts(locationID: Int): InventoryInfoModel

    // общий список ТМЦ для экспорта в Excel
    fun getAllInventoryItemForExport(): List<InventoryItemForExportModel>

    // очистка DB
    suspend fun clearAll(dataBase: MainDataBase): Boolean

    // Информация об 1 ТМЦ по id
    fun getInventoryItemDetail(id: Int): InventoryItemForDetailFullModel

    // update актуального местоположения ТМЦ по id
    fun updateLocationInventoryItemByID(locationID: Int, location: String, id: Int)

    // общий список ТМЦ для сканирования
    fun getAllInventoryItemListForRfidScanning(): List<InventoryItemForScanningModel>

    // reset актуального местоположения ТМЦ по id
    fun resetLocationInventoryItemByID(id: Int)

    // Установить примечание
    fun setCommentInventoryItemByID(id: Int, comment: String)

    // Установить статус найдено вручную
    fun setFoundInventoryItemByID(id: Int)

    // проверка загружены ли данные по API
    fun isDataFromApi(): Boolean
}
