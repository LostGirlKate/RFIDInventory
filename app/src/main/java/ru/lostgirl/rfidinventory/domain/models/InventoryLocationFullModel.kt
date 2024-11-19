package ru.lostgirl.rfidinventory.domain.models

import ru.lostgirl.rfidinventory.data.storage.models.InventoryLocation

// Модель данных справочника местоположения
data class InventoryLocationFullModel(
    val id: Int?,
    val name: String,
) {
    fun toInventoryLocation() = InventoryLocation(
        id = this.id,
        name = this.name
    )
}
