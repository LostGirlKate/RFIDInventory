package ru.lostgirl.rfidinventory.domain.models

import ru.lostgirl.rfidinventory.R

const val INVENTORY_ITEM_STATE_FOUND = "Найдено"
const val INVENTORY_ITEM_STATE_NOT_FOUND = "Не найдено"
const val INVENTORY_ITEM_STATE_FOUND_IN_WRONG_PLACE = "Найдено не на своем месте"

// Статусы ТМЦ
enum class InventoryItemState {
    STATE_FOUND, STATE_NOT_FOUND, STATE_FOUND_IN_WRONG_PLACE
}

// преобразование статуса ТМЦ в строку
fun InventoryItemState.toStr() = when (this) {
    InventoryItemState.STATE_FOUND -> INVENTORY_ITEM_STATE_FOUND
    InventoryItemState.STATE_NOT_FOUND -> INVENTORY_ITEM_STATE_NOT_FOUND
    InventoryItemState.STATE_FOUND_IN_WRONG_PLACE -> INVENTORY_ITEM_STATE_FOUND_IN_WRONG_PLACE
}

// преобразовать в цвет
fun InventoryItemState.toColor() = when (this) {
    InventoryItemState.STATE_FOUND -> R.color.green
    InventoryItemState.STATE_NOT_FOUND -> R.color.red
    InventoryItemState.STATE_FOUND_IN_WRONG_PLACE -> R.color.orange
}

fun InventoryItemState.toIco() = when (this) {
    InventoryItemState.STATE_FOUND -> R.drawable.ico_found
    InventoryItemState.STATE_NOT_FOUND -> R.drawable.ico_not_found
    InventoryItemState.STATE_FOUND_IN_WRONG_PLACE -> R.drawable.ico_found_in_wrong_place
}