package ru.lostgirl.rfidinventory.mvi

interface ViewModelContract<EVENT> {
    fun process(viewEvent: EVENT)
}
