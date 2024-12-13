package ru.lostgirl.rfidinventory.domain.usecase

import ru.lostgirl.rfidinventory.domain.repository.BarcodeReaderRepository

// use case Остановить 2D сканер
class StopBarcodeReaderUseCase(private val repository: BarcodeReaderRepository) {
    fun execute() {
        repository.stop()
    }
}
