package ru.lostgirl.rfidinventory.domain.usecase.api

import ru.lostgirl.rfidinventory.domain.repository.ApiRepository

// use case Загрузка данных по API
class LoadDataFromApiUseCase(private val apiRepository: ApiRepository) {
    suspend fun execute() {
        apiRepository.load()
    }
}