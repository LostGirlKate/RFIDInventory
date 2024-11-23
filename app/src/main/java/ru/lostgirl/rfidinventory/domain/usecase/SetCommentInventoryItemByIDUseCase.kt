package ru.lostgirl.rfidinventory.domain.usecase

import ru.lostgirl.rfidinventory.domain.repository.InventoryRepository

// use case Установить примечание
class SetCommentInventoryItemByIDUseCase(private val repository: InventoryRepository) {
    fun execute(id: Int, comment: String) {
        return repository.setCommentInventoryItemByID(id, comment)
    }
}
