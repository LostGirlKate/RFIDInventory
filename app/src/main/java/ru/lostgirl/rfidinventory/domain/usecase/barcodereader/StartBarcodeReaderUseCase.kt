package ru.lostgirl.rfidinventory.domain.usecase.barcodereader

import ru.lostgirl.rfidinventory.domain.repository.BarcodeReaderRepository

// use case Начать сканирование 2D сканером
class StartBarcodeReaderUseCase(private val repository: BarcodeReaderRepository) {
    fun execute(): Boolean {
        return repository.start()
    }
}
