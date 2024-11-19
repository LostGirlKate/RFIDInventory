package ru.lostgirl.rfidinventory.di

import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import ru.lostgirl.rfidinventory.data.repository.InventoryRepositoryImpl
import ru.lostgirl.rfidinventory.data.storage.InventoryStorage
import ru.lostgirl.rfidinventory.data.storage.roomdb.MainDataBase
import ru.lostgirl.rfidinventory.domain.repository.InventoryRepository
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


    single<ResourcesProvider> {
        ResourcesProvider(context = get())
    }
}
