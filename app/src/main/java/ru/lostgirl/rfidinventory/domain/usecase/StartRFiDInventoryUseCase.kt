package ru.lostgirl.rfidinventory.domain.usecase

import ru.lostgirl.rfidinventory.domain.repository.RFIDReaderRepository

// use case Начать инвентаризацию (чтение меток) RFID ридером
// (необходимо указать мощность с которой запускаем считывание,
// собитие при ошибке старта
// и событие при успешном четнии метки)
class StartRFiDInventoryUseCase(private val repository: RFIDReaderRepository) {
    fun execute(
        power: Int,
        onError: (String) -> Unit,
        onTags: (tagsRaw: List<String>) -> Unit,
    ) {
        repository.startInventory(power, onError, onTags)
    }
}
