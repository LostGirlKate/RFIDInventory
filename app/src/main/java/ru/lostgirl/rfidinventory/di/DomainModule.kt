package ru.lostgirl.rfidinventory.di

import org.koin.dsl.module
import ru.lostgirl.rfidinventory.domain.usecase.ClearDataBaseUseCase
import ru.lostgirl.rfidinventory.domain.usecase.CloseBarcodeReaderUseCase
import ru.lostgirl.rfidinventory.domain.usecase.ExportDataToExcelFileUseCase
import ru.lostgirl.rfidinventory.domain.usecase.GetAllInventoryItemListForScanningUseCase
import ru.lostgirl.rfidinventory.domain.usecase.GetAllInventoryItemUseCase
import ru.lostgirl.rfidinventory.domain.usecase.GetAllLocationsUseCase
import ru.lostgirl.rfidinventory.domain.usecase.GetDataForExcelUseCase
import ru.lostgirl.rfidinventory.domain.usecase.GetDataFromExcelUseCase
import ru.lostgirl.rfidinventory.domain.usecase.GetInventoryInfoUseCase
import ru.lostgirl.rfidinventory.domain.usecase.GetInventoryItemByLocationIDUseCase
import ru.lostgirl.rfidinventory.domain.usecase.GetInventoryItemDetailUseCase
import ru.lostgirl.rfidinventory.domain.usecase.GetRFIDReaderPowerUseCase
import ru.lostgirl.rfidinventory.domain.usecase.InitRFIDReaderUseCase
import ru.lostgirl.rfidinventory.domain.usecase.IsRFIDReaderInitializedUseCase
import ru.lostgirl.rfidinventory.domain.usecase.LoadDataToDataBaseUseCase
import ru.lostgirl.rfidinventory.domain.usecase.OpenBarcodeReaderUseCase
import ru.lostgirl.rfidinventory.domain.usecase.ResetLocationInventoryItemByIDUseCase
import ru.lostgirl.rfidinventory.domain.usecase.SetCommentInventoryItemByIDUseCase
import ru.lostgirl.rfidinventory.domain.usecase.SetFoundInventoryItemByIDUseCase
import ru.lostgirl.rfidinventory.domain.usecase.StartBarcodeReaderUseCase
import ru.lostgirl.rfidinventory.domain.usecase.StartRFiDInventoryUseCase
import ru.lostgirl.rfidinventory.domain.usecase.StopBarcodeReaderUseCase
import ru.lostgirl.rfidinventory.domain.usecase.StopRFIDInventoryUseCase
import ru.lostgirl.rfidinventory.domain.usecase.StopRFIDReaderUseCase
import ru.lostgirl.rfidinventory.domain.usecase.UpdateInventoryItemUseCase

val domainModule = module {

    factory<ClearDataBaseUseCase> {
        ClearDataBaseUseCase(inventoryRepository = get(), dataBase = get())
    }

    factory<ExportDataToExcelFileUseCase> {
        ExportDataToExcelFileUseCase(resourcesProvider = get())
    }

    factory<GetAllInventoryItemListForScanningUseCase> {
        GetAllInventoryItemListForScanningUseCase(inventoryRepository = get())
    }

    factory<GetAllInventoryItemUseCase> {
        GetAllInventoryItemUseCase(inventoryRepository = get())
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

    factory<UpdateInventoryItemUseCase> {
        UpdateInventoryItemUseCase(inventoryRepository = get())
    }

    factory<InitRFIDReaderUseCase> {
        InitRFIDReaderUseCase(
            reader = get()
        )
    }

    factory<StartRFiDInventoryUseCase> {
        StartRFiDInventoryUseCase(repository = get())
    }

    factory<StopRFIDInventoryUseCase> {
        StopRFIDInventoryUseCase(repository = get())
    }

    factory<GetRFIDReaderPowerUseCase> {
        GetRFIDReaderPowerUseCase(repository = get())
    }

    factory<StopRFIDReaderUseCase> {
        StopRFIDReaderUseCase(repository = get())
    }

    factory<IsRFIDReaderInitializedUseCase> {
        IsRFIDReaderInitializedUseCase(repository = get())
    }

    factory<OpenBarcodeReaderUseCase> {
        OpenBarcodeReaderUseCase(reader = get())
    }

    factory<StartBarcodeReaderUseCase> {
        StartBarcodeReaderUseCase(repository = get())
    }

    factory<StopBarcodeReaderUseCase> {
        StopBarcodeReaderUseCase(repository = get())
    }

    factory<CloseBarcodeReaderUseCase> {
        CloseBarcodeReaderUseCase(repository = get())
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