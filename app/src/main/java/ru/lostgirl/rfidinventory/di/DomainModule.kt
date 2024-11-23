package ru.lostgirl.rfidinventory.di

import org.koin.dsl.module
import ru.lostgirl.rfidinventory.domain.usecase.ClearDataBaseUseCase
import ru.lostgirl.rfidinventory.domain.usecase.ExportDataToExcelFileUseCase
import ru.lostgirl.rfidinventory.domain.usecase.GetAllLocationsUseCase
import ru.lostgirl.rfidinventory.domain.usecase.GetDataForExcelUseCase
import ru.lostgirl.rfidinventory.domain.usecase.GetDataFromExcelUseCase
import ru.lostgirl.rfidinventory.domain.usecase.GetInventoryInfoUseCase
import ru.lostgirl.rfidinventory.domain.usecase.GetInventoryItemByLocationIDUseCase
import ru.lostgirl.rfidinventory.domain.usecase.GetInventoryItemDetailUseCase
import ru.lostgirl.rfidinventory.domain.usecase.LoadDataToDataBaseUseCase
import ru.lostgirl.rfidinventory.domain.usecase.ResetLocationInventoryItemByIDUseCase
import ru.lostgirl.rfidinventory.domain.usecase.SetCommentInventoryItemByIDUseCase
import ru.lostgirl.rfidinventory.domain.usecase.SetFoundInventoryItemByIDUseCase

val domainModule = module {

    factory<ClearDataBaseUseCase> {
        ClearDataBaseUseCase(inventoryRepository = get(), dataBase = get())
    }

    factory<ExportDataToExcelFileUseCase> {
        ExportDataToExcelFileUseCase(resourcesProvider = get())
    }

    factory<GetAllLocationsUseCase> {
        GetAllLocationsUseCase(inventoryRepository = get())
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

    factory<GetInventoryItemByLocationIDUseCase> {
        GetInventoryItemByLocationIDUseCase(inventoryRepository = get())
    }

    factory<GetInventoryItemDetailUseCase> {
        GetInventoryItemDetailUseCase(repository = get(), resourcesProvider = get())
    }

    factory<LoadDataToDataBaseUseCase> {
        LoadDataToDataBaseUseCase(inventoryRepository = get())
    }

    factory<ResetLocationInventoryItemByIDUseCase> {
        ResetLocationInventoryItemByIDUseCase(repository = get())
    }

    factory<SetFoundInventoryItemByIDUseCase> {
        SetFoundInventoryItemByIDUseCase(repository = get())
    }

    factory<SetCommentInventoryItemByIDUseCase> {
        SetCommentInventoryItemByIDUseCase(repository = get())
    }
}
