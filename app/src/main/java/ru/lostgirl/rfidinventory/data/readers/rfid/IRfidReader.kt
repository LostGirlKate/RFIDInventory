package ru.lostgirl.rfidinventory.data.readers.rfid

import android.content.Context

interface IRfidReader {

    val isRunned: Boolean
    val isReaderInitialized: Boolean
    fun poweron(context: Context): Boolean
    fun poweroff()

    // мощность power 8=min 30=max
    fun start(power: Int, onError: (String) -> Unit, onTags: (tagsRaw: List<String>) -> Unit)
    fun getPower(): Int

    // мощность power 8=min 30=max
    fun startSearch(
        power: Int,
        mask: Pair<Int, String>,
        onError: (String) -> Unit,
        onTags: (tagsRaw: List<Pair<String, Int>>) -> Unit
    )
    fun stop()

    // мощность power в процентах 0=min 100=max
    fun readSingleEpc(power: Int): Triple<String?, Boolean, String?>

    // мощность power в процентах 0=min 100=max
    fun readEpcs(power: Int): Triple<List<String>, Boolean, String?>

    // мощность power в процентах 0=min 100=max
    fun writeSingleEpc(power: Int, targetEpc: String, newEpc: String): Pair<Boolean, String?>

    // мощность power в процентах 0=min 100=max
    fun readSingleReserved(power: Int, targetEpc: String): Pair<String?, String?>

    // мощность power в процентах 0=min 100=max
    fun writeSingleReserved(
        power: Int,
        targetEpc: String,
        newReserved: ByteArray
    ): Pair<Boolean, String?>
}
