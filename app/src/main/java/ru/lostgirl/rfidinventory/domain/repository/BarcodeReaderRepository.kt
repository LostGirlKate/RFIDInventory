package ru.lostgirl.rfidinventory.domain.repository

interface BarcodeReaderRepository {

    // старт сканирования
    fun start(): Boolean

    // остановка сканирования
    fun stop()

    // закрытие 2D сканера
    fun close()

    // установка callback для обработки результата сканировния
    suspend fun setOnSuccess(onSuccess: (String) -> Unit): Boolean
}
