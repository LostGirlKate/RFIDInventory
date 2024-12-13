package ru.lostgirl.rfidinventory.data.api.models

import com.google.gson.annotations.SerializedName
import ru.lostgirl.rfidinventory.data.storage.models.InventoryLocation

// Местоположение (справочник API)
data class Location(
    @SerializedName(value = "location_id")
    val locationId: Int,
    @SerializedName(value = "title")
    val title: String,
) {
    fun toInventoryLocation() = InventoryLocation(
        id = null,
        name = this.title,
        apiID = this.locationId,
    )
}
