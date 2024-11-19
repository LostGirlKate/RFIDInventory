package ru.lostgirl.rfidinventory.domain.models

// Модель данных ТМЦ для экспорта в Excel
data class InventoryItemForExportModel(
    val rowNum: String,
    val inventoryNum: String,
    val managerName: String,
    val location: String,
    val type: String,
    val model: String,
    val serialNum: String,
    val shipmentNum: String,
    val rfidCardNum: String,
    val actualLocation: String,
    val comment: String,
) {
    // Преобразование объекта в List<String> для выгрузки
    fun toListOfString(): List<String> {
        return """$rowNum|
            $inventoryNum|
            $managerName|
            $location|
            $type|
            $model|
            $serialNum|
            $shipmentNum|
            ${if (rfidCardNum == "null" || rfidCardNum.isEmpty()) "" else rfidCardNum}|
            ${if (actualLocation == "null" || actualLocation.isEmpty()) "" else actualLocation}|            
            ${
            if (comment == "null" || comment.isEmpty()) {
                ""
            } else {
                comment.replace(
                    System.getProperty("line.separator"),
                    "; "
                )
            }
        }
        """.split(
            '|'
        )
    }
}
