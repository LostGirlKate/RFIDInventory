package ru.lostgirl.rfidinventory.domain.usecase.barcodereader

import ru.lostgirl.rfidinventory.domain.repository.BarcodeReaderRepository

// use case Закрыть 2D сканнер
class CloseBarcodeReaderUseCase(private val repository: BarcodeReaderRepository) {
    fun execute() {
        repository.close()
    }
}
