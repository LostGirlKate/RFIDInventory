package ru.lostgirl.rfidinventory.data.readers.barcode2D

import android.content.Context
import com.rscja.barcode.BarcodeDecoder
import com.rscja.barcode.BarcodeFactory
import com.rscja.barcode.BarcodeUtility

class Barcode2DReader : BarcodeReader {

    override var isOpen = false
    private var barcodeDecoder =
        BarcodeFactory.getInstance().barcodeDecoder

    // старт сканирования
    override fun start(): Boolean {
        return barcodeDecoder.startScan()
    }

    // остановка сканирования
    override fun stop() {
        barcodeDecoder.stopScan()
    }

    // закрытие 2D сканера
    override fun close() {
        isOpen = false
        barcodeDecoder.close()
    }

    // инициализация 2D сканера с установкой callback для обработки результата сканировния
    override fun open(context: Context): Boolean {
        isOpen = barcodeDecoder.open(context)
        BarcodeUtility.getInstance().enableVibrate(context, true)
        return isOpen
    }

    override suspend fun setOnSuccess(onSuccess: (String) -> Unit): Boolean {
        barcodeDecoder.setDecodeCallback { barcodeEntity ->
            if (barcodeEntity.resultCode == BarcodeDecoder.DECODE_SUCCESS) {
                onSuccess(barcodeEntity.barcodeData)
            }
        }
        return isOpen
    }
}
