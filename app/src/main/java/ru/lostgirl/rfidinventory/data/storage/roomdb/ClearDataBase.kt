package ru.lostgirl.rfidinventory.data.storage.roomdb

// Очистка DB + сброс sequence (автогенераторы id)
object ClearDataBase {
    fun execute(dataBase: MainDataBase) {
        dataBase.clearAllTables()
        dataBase.openHelper.writableDatabase.execSQL("DELETE FROM sqlite_sequence")
    }
}
