package ru.lostgirl.rfidinventory.di

import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import ru.lostgirl.rfidinventory.data.readers.barcode2D.Barcode2DReader
import ru.lostgirl.rfidinventory.data.readers.barcode2D.BarcodeReader
import ru.lostgirl.rfidinventory.data.readers.rfid.IRfidReader
import ru.lostgirl.rfidinventory.data.readers.rfid.Reader
import ru.lostgirl.rfidinventory.data.repository.BarcodeReaderRepositoryImpl
import ru.lostgirl.rfidinventory.data.repository.InventoryRepositoryImpl
import ru.lostgirl.rfidinventory.data.repository.RFIDReaderRepositoryImpl
import ru.lostgirl.rfidinventory.data.storage.InventoryStorage
import ru.lostgirl.rfidinventory.data.storage.roomdb.MainDataBase
import ru.lostgirl.rfidinventory.domain.repository.BarcodeReaderRepository
import ru.lostgirl.rfidinventory.domain.repository.InventoryRepository
import ru.lostgirl.rfidinventory.domain.repository.RFIDReaderRepository
import ru.lostgirl.rfidinventory.utils.ResourcesProvider

val dataModule = module {

    single<MainDataBase> {
        Room.databaseBuilder(
            androidApplication(),
            MainDataBase::class.java,
            "inventory.db"
        )
            .allowMainThreadQueries()
            .build()
    }

    single<InventoryStorage> {
        get<MainDataBase>().getDao()
    }

    single<InventoryRepository> {
        InventoryRepositoryImpl(inventoryStorage = get())
    }

    single<IRfidReader> {
        val reader = Reader()
        reader.poweron(context = get())
        reader
    }

    single<RFIDReaderRepository> {
        RFIDReaderRepositoryImpl(reader = get())
    }

    single<BarcodeReader> {
        val reader = Barcode2DReader()
        reader.open(context = get())
        reader
    }

    single<BarcodeReaderRepository> {
        BarcodeReaderRepositoryImpl(reader = get())
    }

    single<ResourcesProvider> {
        ResourcesProvider(context = get())
    }
}

