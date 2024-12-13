package ru.lostgirl.rfidinventory.data.repository

import ru.lostgirl.rfidinventory.data.readers.barcode2D.BarcodeReader
import ru.lostgirl.rfidinventory.domain.repository.BarcodeReaderRepository

class BarcodeReaderRepositoryImpl(private val reader: BarcodeReader) : BarcodeReaderRepository {

    // старт сканирования
    override fun start(): Boolean {
        return reader.start()
    }

    // остановка сканирования
    override fun stop() {
        reader.stop()
    }

    // закрытие 2D сканера
    override fun close() {
        reader.close()
    }

    // установка callback для обработки результата сканировния
    override suspend fun setOnSuccess(onSuccess: (String) -> Unit): Boolean {
        return reader.setOnSuccess(onSuccess)
    }
}
