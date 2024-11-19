package ru.lostgirl.rfidinventory.utils

import android.content.ContentResolver
import android.net.Uri
import android.os.Environment
import android.webkit.MimeTypeMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.poi.hssf.usermodel.HSSFDateUtil
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.FormulaEvaluator
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.util.WorkbookUtil
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Locale

object ExcelUtil {
    const val MEM_TYPE_XLS = "application/vnd.ms-excel"
    const val MEM_TYPE_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"

    // Сохранение массива данных в Excel файл
    suspend fun exportDataToExcelFile(
        data: List<List<String>>,
        fileName: String,
        columnNames: List<String>,
    ): Boolean {
        val workbook = SXSSFWorkbook()
        val sheet = workbook.createSheet(WorkbookUtil.createSafeSheetName("mysheet"))
        val row: Row = sheet.createRow(0)
        for ((i, item) in columnNames.withIndex()) {
            val cell = row.createCell(i)
            cell.setCellValue(item)
        }
        for ((i, item) in data.withIndex()) {
            val rowData: Row = sheet.createRow(i + 1)
            for (j in item.indices) {
                val cell = rowData.createCell(j)
                cell.setCellValue(item[j].trim())
            }
        }
        return try {
            val outFile = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                fileName
            )
            withContext(Dispatchers.IO) {
                val outputStream: OutputStream = FileOutputStream(outFile.absolutePath)
                workbook.write(outputStream)
                outputStream.flush()
                outputStream.close()
            }
            true
        } catch (e: Exception) {
            Timber.d("InvMobRFID", e.toString())
            false
        }
    }

    // Получение массива данных из Excel файла
    fun getDataFromExcel(
        uri: Uri,
        contentResolver: ContentResolver,
    ): Array<Array<String>> {
        var resultArray = arrayOf<Array<String>>()
        try {
            val inputStream = contentResolver.openInputStream(uri)
            val mime = MimeTypeMap.getSingleton()
            val extension = mime.getExtensionFromMimeType(contentResolver.getType(uri))
            val workbook =
                if (extension == "xls") HSSFWorkbook(inputStream) else XSSFWorkbook(inputStream)
            val sheet = workbook.getSheetAt(0)
            val rowsCount = sheet.physicalNumberOfRows
            val formulaEvaluator: FormulaEvaluator =
                workbook.creationHelper.createFormulaEvaluator()
            for (r in 0 until rowsCount) {
                val row: Row = sheet.getRow(r)
                val cellsCount: Int = row.physicalNumberOfCells
                var array = arrayOf<String>()
                for (c in 0 until cellsCount) {
                    val value: String = getCellAsString(row, c, formulaEvaluator)
                    array += value.trim()
                }
                resultArray += array
            }
        } catch (e: IOException) {
            Timber.tag("getDataFromExcel").e(e)
        }
        return resultArray
    }

    // Преобразование значения ячейки в строку
    private fun getCellAsString(row: Row, c: Int, formulaEvaluator: FormulaEvaluator): String {
        var value = ""
        try {
            val cell = row.getCell(c)
            val cellValue = formulaEvaluator.evaluate(cell)
            when (cellValue.cellType) {
                Cell.CELL_TYPE_BOOLEAN -> {
                    value = "" + cellValue.booleanValue
                }

                Cell.CELL_TYPE_NUMERIC -> {
                    val numericValue = cellValue.numberValue
                    value = if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        val date = cellValue.numberValue
                        val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.US)
                        formatter.format(HSSFDateUtil.getJavaDate(date))
                    } else {
                        "" + numericValue
                    }
                }

                Cell.CELL_TYPE_STRING -> {
                    value = "" + cellValue.stringValue
                }

                else -> {}
            }
        } catch (e: NullPointerException) {
            Timber.tag("getDataFromExcel").e(e.message!!)
        }
        return value
    }
}
