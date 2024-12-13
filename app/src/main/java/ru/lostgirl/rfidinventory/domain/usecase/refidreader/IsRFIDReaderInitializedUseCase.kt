package ru.lostgirl.rfidinventory.domain.usecase.refidreader

import ru.lostgirl.rfidinventory.domain.repository.RFIDReaderRepository

// use case Получение статуса инициализации RFID ридера
class IsRFIDReaderInitializedUseCase(private val repository: RFIDReaderRepository) {
    fun execute(): Boolean {
        return repository.isReaderInitialized()
    }
}
