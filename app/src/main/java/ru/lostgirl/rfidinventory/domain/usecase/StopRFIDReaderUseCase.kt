package ru.lostgirl.rfidinventory.domain.usecase

import ru.lostgirl.rfidinventory.domain.repository.RFIDReaderRepository

// use case Остановить RFID ридер
class StopRFIDReaderUseCase(private val repository: RFIDReaderRepository) {
    fun execute() {
        repository.stopReader()
    }
}
