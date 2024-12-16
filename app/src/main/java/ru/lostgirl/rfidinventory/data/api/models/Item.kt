package ru.lostgirl.rfidinventory.data.api.models

import com.google.gson.annotations.SerializedName
import ru.lostgirl.rfidinventory.data.storage.models.InventoryItem

// Единица инвентаризации (ТМЦ) API
// itemId - идентификатор
// inventoryNum - инвентарный номер бухгалтерии
// managerName - сотрудник
// locationId - id местоположения
// type - тип
// model - модель
// serialNum - серийный номер
// shipmentNum - номер партии (шк EAN-13)
// rfidCardNum - RFID Dec
// comment - примечание

data class Item(
    @SerializedName(value = "item_id")
    val itemId: Int?,
    @SerializedName(value = "inventory_num")
    val inventoryNum: String,
    @SerializedName(value = "manager_name")
    val managerName: String,
    @SerializedName(value = "location_id")
    val locationId: Int?,
    @SerializedName(value = "type")
    val type: String,
    @SerializedName(value = "model")
    val model: String,
    @SerializedName(value = "serial_num")
    val serialNum: String?,
    @SerializedName(value = "shipment_num")
    val shipmentNum: String?,
    @SerializedName(value = "rfid_card_num")
    val rfidCardNum: String?,
    @SerializedName(value = "comment")
    val comment: String?,
) {
    fun toInventoryItem(localLocationID: Int, localLocation: String) = InventoryItem(
        id = null,
        inventoryNum = this.inventoryNum,
        managerName = this.managerName,
        locationID = localLocationID,
        location = localLocation,
        type = this.type,
        model = this.model,
        serialNum = this.serialNum ?: "",
        shipmentNum = this.shipmentNum ?: "",
        rfidCardNum = this.rfidCardNum ?: "",
        actualLocationID = null,
        actualLocation = null,
        prevLocationID = null,
        prevLocation = null,
        comment = this.comment,
        apiID = this.itemId
    )
}

data class ItemForSave(
    @SerializedName(value = "item_id")
    val itemId: Int?,
    @SerializedName(value = "location_id")
    val locationId: Int?,
    @SerializedName(value = "comment")
    val comment: String?,
)