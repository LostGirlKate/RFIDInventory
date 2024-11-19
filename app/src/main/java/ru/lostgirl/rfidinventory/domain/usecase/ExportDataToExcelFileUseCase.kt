package ru.lostgirl.rfidinventory.domain.usecase

import ru.lostgirl.rfidinventory.R
import ru.lostgirl.rfidinventory.utils.ExcelUtil
import ru.lostgirl.rfidinventory.utils.ResourcesProvider

// use case Выгрузить данные в Excel файл
class ExportDataToExcelFileUseCase(private val resourcesProvider: ResourcesProvider) {
    suspend fun execute(
        data: List<List<String>>,
        fileName: String,
    ): Boolean {
        val columnNames = listOf(
            resourcesProvider.getString(R.string.num),
            resourcesProvider.getString(R.string.inventory_num),
            resourcesProvider.getString(R.string.manager_name),
            resourcesProvider.getString(R.string.location),
            resourcesProvider.getString(R.string.type),
            resourcesProvider.getString(R.string.model),
            resourcesProvider.getString(R.string.serial_num),
            resourcesProvider.getString(R.string.shipment_num),
            resourcesProvider.getString(R.string.rfid_dec),
            resourcesProvider.getString(R.string.location_fact),
            resourcesProvider.getString(R.string.comment)
        )
        return ExcelUtil.exportDataToExcelFile(data, fileName, columnNames)
    }
}
