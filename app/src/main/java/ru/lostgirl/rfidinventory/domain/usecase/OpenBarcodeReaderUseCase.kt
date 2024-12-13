package ru.lostgirl.rfidinventory.domain.usecase

import ru.lostgirl.rfidinventory.data.readers.barcode2D.BarcodeReader

// use case Открытие(инициализация) 2D сканера
class OpenBarcodeReaderUseCase(
    private val reader: BarcodeReader,
) {
    suspend fun execute(onSuccess: (String) -> Unit): Boolean {
        return reader.setOnSuccess(onSuccess)
    }
}
