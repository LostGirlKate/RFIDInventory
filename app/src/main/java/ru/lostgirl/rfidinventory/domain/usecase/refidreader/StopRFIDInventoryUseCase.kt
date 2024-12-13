package ru.lostgirl.rfidinventory.domain.usecase.refidreader

import ru.lostgirl.rfidinventory.domain.repository.RFIDReaderRepository

// use case Остановаить инвентаризацию(чтение меток) RFID ридером
class StopRFIDInventoryUseCase(private val repository: RFIDReaderRepository) {
    fun execute() {
        repository.stopInventory()
    }
}
