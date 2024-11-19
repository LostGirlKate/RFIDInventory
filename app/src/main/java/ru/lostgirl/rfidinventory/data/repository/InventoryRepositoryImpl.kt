package ru.lostgirl.rfidinventory.data.repository

import ru.lostgirl.rfidinventory.data.storage.InventoryStorage
import ru.lostgirl.rfidinventory.data.storage.roomdb.ClearDataBase
import ru.lostgirl.rfidinventory.data.storage.roomdb.MainDataBase
import ru.lostgirl.rfidinventory.domain.models.InventoryInfoModel
import ru.lostgirl.rfidinventory.domain.models.InventoryItemForDetailFullModel
import ru.lostgirl.rfidinventory.domain.models.InventoryItemForExportModel
import ru.lostgirl.rfidinventory.domain.models.InventoryItemForListModel
import ru.lostgirl.rfidinventory.domain.models.InventoryItemForScanningModel
import ru.lostgirl.rfidinventory.domain.models.InventoryItemFullModel
import ru.lostgirl.rfidinventory.domain.models.InventoryLocationFullModel
import ru.lostgirl.rfidinventory.domain.repository.InventoryRepository

class InventoryRepositoryImpl(
    private val inventoryStorage: InventoryStorage,
) : InventoryRepository {

    override suspend fun insertInventoryLocation(location: InventoryLocationFullModel): Boolean {
        val inventoryLocation = location.toInventoryLocation()
        inventoryStorage.insertInventoryLocation(inventoryLocation)
        return true
    }

    override fun getAllLocationList(): List<InventoryLocationFullModel> {
        return inventoryStorage.getAllLocationList().map { it.toInventoryLocationFullModel() }
    }

    override fun getAllInventoryFullList(): List<InventoryItemFullModel> {
        return inventoryStorage.getAllInventoryItemList().map { it.toInventoryItemFullModel() }
    }

    override fun insertManyInventoryItem(items: List<InventoryItemFullModel>): List<Long> {
        return inventoryStorage.insertManyInventoryItem(items.map { it.toInventoryItem() })
    }

    override fun getInventoryItemByLocationID(locationID: Int): List<InventoryItemForListModel> {
        return inventoryStorage.getInventoryItemByLocationID(locationID)
            .map { it.toInventoryItemModelForList() }
    }

    override fun getInventoryItemsCounts(locationID: Int): InventoryInfoModel {
        return inventoryStorage.getInventoryItemsCounts(locationID)
            .map { it.toInventoryInfoModel() }.first()
    }

    override fun getAllInventoryItemForExport(): List<InventoryItemForExportModel> {
        return inventoryStorage.getAllInventoryItemList().mapIndexed { index, inventoryItem ->
            inventoryItem.toInventoryItemForExportModel(index + 1)
        }
    }

    override suspend fun clearAll(dataBase: MainDataBase): Boolean {
        ClearDataBase.execute(dataBase)
        return true
    }

    override fun getInventoryItemDetail(id: Int): InventoryItemForDetailFullModel {
        return inventoryStorage.getInventoryItemDetail(id).toInventoryItemFullModelForDetail()
    }

    override fun updateLocationInventoryItemByID(locationID: Int, location: String, id: Int) {
        inventoryStorage.updateLocationInventoryItemByID(locationID, location, id)
    }

    override fun getAllInventoryItemListForRfidScanning(): List<InventoryItemForScanningModel> {
        return inventoryStorage.getAllInventoryItemList()
            .map { it.toInventoryItemForScanningModel() }
    }

    override fun resetLocationInventoryItemByID(id: Int) {
        inventoryStorage.resetLocationInventoryItemByID(id)
    }

    override fun setCommentInventoryItemByID(id: Int, comment: String) {
        inventoryStorage.setCommentInventoryItemByID(id, comment)
    }

    override fun setFoundInventoryItemByID(id: Int) {
        inventoryStorage.setFoundInventoryItemByID(id)
    }
}
