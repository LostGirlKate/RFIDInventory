package ru.lostgirl.rfidinventory.domain.usecase.refidreader

import ru.lostgirl.rfidinventory.domain.repository.RFIDReaderRepository

// use case Получение можности RFID ридера
class GetRFIDReaderPowerUseCase(private val repository: RFIDReaderRepository) {
    fun execute(): Int {
        return repository.getPower()
    }
}
