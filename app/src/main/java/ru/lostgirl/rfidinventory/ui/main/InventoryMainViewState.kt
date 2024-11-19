package ru.lostgirl.rfidinventory.ui.main

import ru.lostgirl.rfidinventory.R
import ru.lostgirl.rfidinventory.domain.models.InventoryInfoModel
import ru.lostgirl.rfidinventory.domain.models.InventoryState

data class InventoryMainViewState(
    val inventoryState: InventoryInfoModel,
) {
    // Показвать ли блок со статусом инвентаризации (показывается если данные были загружены)
    val mainInfoBlockVisible = (inventoryState.inventoryState != InventoryState.STATE_NOT_START)

    // Видна ли кнопка открытия инвентаризации (показывается если данные были загружены)
    val openInventoryButtonVisible =
        (inventoryState.inventoryState != InventoryState.STATE_NOT_START)

    // Видна ли кнопка завершения инвентаризации (показывается если данные были загружены)
    val closeInventoryButtonVisible =
        (inventoryState.inventoryState != InventoryState.STATE_NOT_START)

    // Видна ли кнопка выгрузки данных в Excel (показывается если данные были загружены)
    val exportButtonVisible = (inventoryState.inventoryState != InventoryState.STATE_NOT_START)

    // Видна ли кнопка загрузки данных (показывается если данные НЕ были загружены)
    val loadFromFileButtonVisible =
        (inventoryState.inventoryState == InventoryState.STATE_NOT_START)

    // Видно ли сообщение о необходимости загрузки данных (показывается если данные НЕ были загружены)
    val loadingWarningTextVisible =
        (inventoryState.inventoryState == InventoryState.STATE_NOT_START)

    // Цвет текста процентов выполнения инвентаризации
    val progressPercentTextColor =
        when (inventoryState.inventoryState) {
            InventoryState.STATE_READY -> R.color.textColor
            else -> R.color.textColor
        }
}
