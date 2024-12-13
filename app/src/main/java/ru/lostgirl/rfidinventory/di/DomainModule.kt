package ru.lostgirl.rfidinventory.di

import org.koin.dsl.module
import ru.lostgirl.rfidinventory.domain.usecase.ClearDataBaseUseCase
import ru.lostgirl.rfidinventory.domain.usecase.GetAllInventoryItemListForScanningUseCase
import ru.lostgirl.rfidinventory.domain.usecase.GetAllInventoryItemUseCase
import ru.lostgirl.rfidinventory.domain.usecase.GetAllLocationsUseCase
import ru.lostgirl.rfidinventory.domain.usecase.GetInventoryInfoUseCase
import ru.lostgirl.rfidinventory.domain.usecase.GetInventoryItemByLocationIDUseCase
import ru.lostgirl.rfidinventory.domain.usecase.GetInventoryItemDetailUseCase
import ru.lostgirl.rfidinventory.domain.usecase.LoadDataToDataBaseUseCase
import ru.lostgirl.rfidinventory.domain.usecase.ResetLocationInventoryItemByIDUseCase
import ru.lostgirl.rfidinventory.domain.usecase.SetCommentInventoryItemByIDUseCase
import ru.lostgirl.rfidinventory.domain.usecase.SetFoundInventoryItemByIDUseCase
import ru.lostgirl.rfidinventory.domain.usecase.UpdateInventoryItemUseCase
import ru.lostgirl.rfidinventory.domain.usecase.api.ExportDataToApiUseCase
import ru.lostgirl.rfidinventory.domain.usecase.api.LoadDataFromApiUseCase
import ru.lostgirl.rfidinventory.domain.usecase.barcodereader.CloseBarcodeReaderUseCase
import ru.lostgirl.rfidinventory.domain.usecase.barcodereader.OpenBarcodeReaderUseCase
import ru.lostgirl.rfidinventory.domain.usecase.barcodereader.StartBarcodeReaderUseCase
import ru.lostgirl.rfidinventory.domain.usecase.barcodereader.StopBarcodeReaderUseCase
import ru.lostgirl.rfidinventory.domain.usecase.excel.ExportDataToExcelFileUseCase
import ru.lostgirl.rfidinventory.domain.usecase.excel.GetDataForExcelUseCase
import ru.lostgirl.rfidinventory.domain.usecase.excel.GetDataFromExcelUseCase
import ru.lostgirl.rfidinventory.domain.usecase.refidreader.GetRFIDReaderPowerUseCase
import ru.lostgirl.rfidinventory.domain.usecase.refidreader.InitRFIDReaderUseCase
import ru.lostgirl.rfidinventory.domain.usecase.refidreader.IsRFIDReaderInitializedUseCase
import ru.lostgirl.rfidinventory.domain.usecase.refidreader.StartRFiDInventoryUseCase
import ru.lostgirl.rfidinventory.domain.usecase.refidreader.StopRFIDInventoryUseCase
import ru.lostgirl.rfidinventory.domain.usecase.refidreader.StopRFIDReaderUseCase

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

    factory<LoadDataFromApiUseCase> {
        LoadDataFromApiUseCase(apiRepository = get())
    }

    factory<ExportDataToApiUseCase> {
        ExportDataToApiUseCase(apiRepository = get())
    }
}