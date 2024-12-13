package ru.lostgirl.rfidinventory.domain.usecase

import ru.lostgirl.rfidinventory.domain.repository.RFIDReaderRepository

// use case Получение статуса инициализации RFID ридера
class IsRFIDReaderInitializedUseCase(private val repository: RFIDReaderRepository) {
    fun execute(): Boolean {
        return repository.isReaderInitialized()
    }
}
