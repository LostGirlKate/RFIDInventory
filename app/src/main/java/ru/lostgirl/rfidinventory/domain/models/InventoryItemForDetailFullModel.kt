package ru.lostgirl.rfidinventory.domain.models

import ru.lostgirl.rfidinventory.R
import ru.lostgirl.rfidinventory.utils.ResourcesProvider

// Модель данных ТМЦ с полным списком параметров (+статус для отображения) для окна детализации
data class InventoryItemForDetailFullModel(
    val id: Int?,
    val inventoryNum: String,
    val managerName: String,
    val location: String,
    val type: String,
    val model: String,
    val serialNum: String,
    val shipmentNum: String,
    val rfidCardNum: String,
    val actualLocation: String?,
    val status: String,
    val comment: String?,
) {
    // Преобразование объекта в список параметров
    fun toListOfDetail(
        resourcesProvider: ResourcesProvider,
        statusColor: Int,
    ): List<InventoryItemDetailModel> {
        return this.let {
            listOf(
                InventoryItemDetailModel(
                    resourcesProvider.getString(R.string.status),
                    it.status,
                    true,
                    statusColor
                ),
                InventoryItemDetailModel(resourcesProvider.getString(R.string.type), it.type),
                InventoryItemDetailModel(resourcesProvider.getString(R.string.model), it.model),
                InventoryItemDetailModel(
                    resourcesProvider.getString(R.string.location),
                    it.location
                ),
                InventoryItemDetailModel(
                    resourcesProvider.getString(R.string.location_fact),
                    it.actualLocation.orEmpty(),
                    isActualLocation = true
                ),
                InventoryItemDetailModel(
                    resourcesProvider.getString(R.string.inventory_num),
                    it.inventoryNum
                ),
                InventoryItemDetailModel(
                    resourcesProvider.getString(R.string.serial_num),
                    it.serialNum
                ),
                InventoryItemDetailModel(
                    resourcesProvider.getString(R.string.shipment_num),
                    it.shipmentNum
                ),
                InventoryItemDetailModel(
                    resourcesProvider.getString(R.string.rfid_dec),
                    it.rfidCardNum
                ),
                InventoryItemDetailModel(
                    resourcesProvider.getString(R.string.manager_name),
                    it.managerName
                ),
                InventoryItemDetailModel(
                    resourcesProvider.getString(R.string.comment),
                    it.comment.orEmpty()
                )
            )
        }
    }
}
