package ru.lostgirl.rfidinventory.data.readers.rfid

const val MIN_POWER_VALUE = 8.0
const val MAX_POWER_VALUE = 30.0
const val PERCENTAGE = 100.0
const val DIF_POWER = MAX_POWER_VALUE - MIN_POWER_VALUE

// мощность ридера фиксирована power 8=min 30=max - пересчет в проценты и обратно
fun Int.toPower(): Int {
    return (DIF_POWER / PERCENTAGE * this + MIN_POWER_VALUE).toInt()
}

fun Int.toPowerPercent(): Int {
    return ((this - MIN_POWER_VALUE) / DIF_POWER * PERCENTAGE).toInt()
}
