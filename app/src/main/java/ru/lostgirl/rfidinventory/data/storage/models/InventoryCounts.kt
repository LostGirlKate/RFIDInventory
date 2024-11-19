package ru.lostgirl.rfidinventory.data.storage.models


import ru.lostgirl.rfidinventory.domain.models.InventoryInfoModel
import ru.lostgirl.rfidinventory.domain.models.InventoryState
import java.text.DecimalFormat

// Информация об общем количестве объектов для инвентаризации
// countAll - общее кол-во
// countFound - кол-во найденных меток
// countNotFound - кол-во не найденных меток
// countFoundInWrongPlace - кол-во найденных не на своем месте меток
data class InventoryCounts(
    val countAll: Int,
    val countFound: Int,
    val countNotFound: Int,
    val countFoundInWrongPlace: Int,
) {
    fun toInventoryInfoModel() =
        InventoryInfoModel(
            countAllString = this.countAll.formatterToString(),
            countFoundString = this.countFound.formatterToString(),
            countNotFoundString = this.countNotFound.formatterToString(),
            countFoundInWrongPlaceString = this.countFoundInWrongPlace.formatterToString(),
            percentFound = getPercentFound(),
            percentFoundString = "${getPercentFound()}%",
            inventoryState = getState(),
            percentScanningFound = getPercentScanningFound(),
            percentScanningString = "${getPercentScanningFound()}%",
        )

    // расчет процента выполнения инвентаризации
    private fun getPercentFound() =
        if (this.countAll > 0) {
            ((this.countFound + this.countFoundInWrongPlace) / (this.countAll.toDouble()) * 100).toInt()
        } else {
            0
        }

    // расчет процента выполнения инвентаризации при сканирование, учитываем только зеленые
    private fun getPercentScanningFound() =
        if ((this.countNotFound + this.countFound) > 0) {
            ((this.countFound) / ((this.countNotFound + this.countFound).toDouble()) * 100).toInt()
        } else {
            0
        }

    // расчет статуса инвентаризации на основании общего и найденного кол-ва меток
    private fun getState() = when (this.countAll) {
        0 -> InventoryState.STATE_NOT_START
        (this.countFound + this.countFoundInWrongPlace) -> InventoryState.STATE_READY
        else -> InventoryState.STATE_WORK
    }

    // форматирование числового показателя для отображения
    private fun Int.formatterToString() =
        DecimalFormat("#,###")
            .format(this)
            .replace(",", " ")
}
