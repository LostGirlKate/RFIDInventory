package ru.lostgirl.rfidinventory.domain.repository

import android.content.Context

interface RFIDReaderRepository {

    // инициализация ридера
    suspend fun initReader(context: Context): Boolean

    // остановка ридера
    fun stopReader()

    // старт инвентаризации (сканирования)
    fun startInventory(
        power: Int,
        onError: (String) -> Unit,
        onTags: (tagsRaw: List<String>) -> Unit,
    )

    // остановка инвентаризации (сканирования)
    fun stopInventory()

    // получение можности ридера в процентах
    fun getPower(): Int

    // получение статуса инициализации ридера
    fun isReaderInitialized(): Boolean
}
