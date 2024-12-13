package ru.lostgirl.rfidinventory.domain.usecase

import ru.lostgirl.rfidinventory.data.readers.rfid.IRfidReader

// use case Инициализация RFID ридера
class InitRFIDReaderUseCase(
    private val reader: IRfidReader,
) {
    suspend fun execute(): Boolean {
        return reader.isReaderInitialized
    }
}
