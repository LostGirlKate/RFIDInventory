package ru.lostgirl.rfidinventory.di

import org.koin.dsl.module
import ru.lostgirl.rfidinventory.domain.usecase.ClearDataBaseUseCase
import ru.lostgirl.rfidinventory.domain.usecase.ExportDataToExcelFileUseCase
import ru.lostgirl.rfidinventory.domain.usecase.GetDataForExcelUseCase
import ru.lostgirl.rfidinventory.domain.usecase.GetDataFromExcelUseCase
import ru.lostgirl.rfidinventory.domain.usecase.GetInventoryInfoUseCase
import ru.lostgirl.rfidinventory.domain.usecase.LoadDataToDataBaseUseCase

val domainModule = module {

    factory<ClearDataBaseUseCase> {
        ClearDataBaseUseCase(inventoryRepository = get(), dataBase = get())
    }

    factory<ExportDataToExcelFileUseCase> {
        ExportDataToExcelFileUseCase(resourcesProvider = get())
    }

    factory<GetDataForExcelUseCase> {
        GetDataForExcelUseCase(inventoryRepository = get())
    }

    factory<GetDataFromExcelUseCase> {
        GetDataFromExcelUseCase()
    }

    factory<GetInventoryInfoUseCase> {
        GetInventoryInfoUseCase(inventoryRepository = get())
    }

    factory<LoadDataToDataBaseUseCase> {
        LoadDataToDataBaseUseCase(inventoryRepository = get())
    }
}
