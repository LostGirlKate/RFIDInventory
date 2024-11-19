package ru.lostgirl.rfidinventory.domain.models

// Модель для отображения информации о статусе инвентаризации
// countAllString - общее кол-во ТМЦ
// countFoundString - кол-во найденных ТМЦ
// countNotFoundString - кол-во не найденных ТМЦ
// countFoundInWrongPlaceString - кол-во найденных не на своем месте ТМЦ
// percentFound - % выполнения инвентризации Int
// percentFoundString - % выполнения инвентризации String
// inventoryState - статус инвентаризации
data class InventoryInfoModel(
    val countAllString: String = "0",
    val countFoundString: String = "0",
    val countNotFoundString: String = "0",
    val countFoundInWrongPlaceString: String = "0",
    val percentFound: Int = 0,
    val percentFoundString: String = "0",
    val percentScanningFound: Int = 0,
    val percentScanningString: String = "0",
    val inventoryState: InventoryState = InventoryState.STATE_NOT_START,
)
