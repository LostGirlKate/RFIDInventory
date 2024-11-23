package ru.lostgirl.rfidinventory.di

import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.lostgirl.rfidinventory.ui.detail.InventoryItemDetailViewModel
import ru.lostgirl.rfidinventory.ui.list.InventoryListViewModel
import ru.lostgirl.rfidinventory.ui.main.InventoryMainViewModel

val appModule = module {

    viewModel<InventoryMainViewModel> {
        InventoryMainViewModel(
            application = androidApplication(),
            getInventoryInfo = get(),
            loadDataToDataBaseUseCase = get(),
            getDataForExcelUseCase = get(),
            clearDataBaseUseCase = get(),
            getDataFromExcelUseCase = get(),
            exportDataToExcelFileUseCase = get()
        )
    }

    viewModel<InventoryListViewModel> {
        InventoryListViewModel(
            application = androidApplication(),
            getAllLocationsUseCase = get(),
            getInventoryInfo = get(),
            getInventoryItemByLocationIDUseCase = get(),
            resetLocationInventoryItemByID = get(),
            setCommentInventoryItemByIDUseCase = get(),
            setFoundInventoryItemByIDUseCase = get()
        )
    }

    viewModel<InventoryItemDetailViewModel> {
        InventoryItemDetailViewModel(
            application = androidApplication(),
            getInventoryItemDetailUseCase = get()
        )
    }

}
