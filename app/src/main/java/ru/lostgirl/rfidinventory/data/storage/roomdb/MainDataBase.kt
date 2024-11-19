package ru.lostgirl.rfidinventory.data.storage.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.lostgirl.rfidinventory.data.storage.models.InventoryItem
import ru.lostgirl.rfidinventory.data.storage.models.InventoryLocation

@Database(entities = [InventoryLocation::class, InventoryItem::class], version = 1)
abstract class MainDataBase : RoomDatabase() {

    abstract fun getDao(): Dao
}
