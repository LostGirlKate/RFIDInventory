package ru.lostgirl.rfidinventory.data.repository

import android.content.Context
import ru.lostgirl.rfidinventory.data.readers.rfid.IRfidReader
import ru.lostgirl.rfidinventory.data.readers.rfid.toPower
import ru.lostgirl.rfidinventory.data.readers.rfid.toPowerPercent
import ru.lostgirl.rfidinventory.domain.repository.RFIDReaderRepository

class RFIDReaderRepositoryImpl(private val reader: IRfidReader) : RFIDReaderRepository {

    // инициализация ридера
    override suspend fun initReader(context: Context): Boolean {
        return reader.poweron(context)
    }

    // остановка ридера
    override fun stopReader() {
        return reader.poweroff()
    }

    // старт инвентаризации (сканирования)
    override fun startInventory(
        power: Int,
        onError: (String) -> Unit,
        onTags: (tagsRaw: List<String>) -> Unit,
    ) {
        reader.start(power.toPower(), onError, onTags)
    }

    // остановка инвентаризации (сканирования)
    override fun stopInventory() {
        reader.stop()
    }

    // получение можности ридера в процентах
    override fun getPower(): Int {
        return reader.getPower().toPowerPercent()
    }

    // получение статуса инициализации ридера
    override fun isReaderInitialized(): Boolean {
        return reader.isReaderInitialized
    }
}
