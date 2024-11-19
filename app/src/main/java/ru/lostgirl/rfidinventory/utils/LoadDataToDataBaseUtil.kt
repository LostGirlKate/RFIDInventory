package ru.lostgirl.rfidinventory.utils


import ru.lostgirl.rfidinventory.domain.models.InventoryItemFullModel
import ru.lostgirl.rfidinventory.domain.models.InventoryLocationFullModel
import ru.lostgirl.rfidinventory.domain.repository.InventoryRepository
import timber.log.Timber

const val COLUMN_INDEX_INVENTORY_NUM = 1
const val COLUMN_INDEX_MANAGER_NAME = 2
const val COLUMN_INDEX_LOADED_LOCATION = 3
const val COLUMN_INDEX_TYPE = 4
const val COLUMN_INDEX_MODEL = 5
const val COLUMN_INDEX_SERIAL_NUM = 6
const val COLUMN_INDEX_SHIPMENT_NUM = 7
const val COLUMN_INDEX_RFID_CARD_NUM = 8

// Загрузка массива данных в BD с учетом индексов колонок
object LoadDataToDataBaseUtil {
    suspend fun load(
        inventoryRepository: InventoryRepository,
        data: Array<Array<String>>,
    ): Boolean {
        var loadedLocationID: Int?
        var loadedLocation: String
        var result = true
        val itemList: ArrayList<InventoryItemFullModel> = arrayListOf()
        try {
            var allLocation = inventoryRepository.getAllLocationList()
            for ((index, array) in data.withIndex()) {
                // пропускаем заголовок
                if (index == 0) {
                    continue
                }
                loadedLocation = array[COLUMN_INDEX_LOADED_LOCATION]
                // если местоположение не найдено - добавляем его в  справочник
                if (allLocation.none { it.name == loadedLocation }) {
                    inventoryRepository.insertInventoryLocation(
                        InventoryLocationFullModel(null, loadedLocation)
                    )
                    allLocation = inventoryRepository.getAllLocationList()
                }
                loadedLocationID = allLocation.firstOrNull { it.name == loadedLocation }?.id ?: 0
                // добавлем ТМЦ в список
                itemList.add(
                    getInventoryItemByArray(array, loadedLocationID)
                )
            }
            // сохраняем весь список ТМЦ в BD
            inventoryRepository.insertManyInventoryItem(itemList)
        } catch (ex: Exception) {
            Timber.tag("LoadDataToDataBaseUtil").e(ex)
            result = false
        }
        return result
    }

    // Объект ТМЦ из массива строк
    private fun getInventoryItemByArray(
        array: Array<String>,
        loadedLocationID: Int,
    ): InventoryItemFullModel {
        val inventoryNum = array[COLUMN_INDEX_INVENTORY_NUM]
        val managerName = array[COLUMN_INDEX_MANAGER_NAME]
        val loadedLocation = array[COLUMN_INDEX_LOADED_LOCATION]
        val type = array[COLUMN_INDEX_TYPE]
        val model = array[COLUMN_INDEX_MODEL]
        val serialNum = array[COLUMN_INDEX_SERIAL_NUM]
        val shipmentNum = array[COLUMN_INDEX_SHIPMENT_NUM]
        val rfidCardNum = array[COLUMN_INDEX_RFID_CARD_NUM]
        return InventoryItemFullModel(
            id = null,
            model = model,
            rfidCardNum = rfidCardNum,
            actualLocation = null,
            locationID = loadedLocationID,
            actualLocationID = null,
            location = loadedLocation,
            inventoryNum = inventoryNum,
            managerName = managerName,
            type = type,
            serialNum = serialNum,
            shipmentNum = shipmentNum,
            prevLocationID = null,
            prevLocation = null,
            comment = null
        )
    }
}
