package com.imgok.ciontek

import com.ctk.sdk.PosApiHelper

object CiontekPrintHelper {
    private val posApiHelper: PosApiHelper = PosApiHelper.getInstance()

    @Volatile
    private var fontPath: String = "/storage/emulated/0/Download/ciontek-printer-font.ttf"

    @Volatile
    private var initialized: Boolean = false

    private const val ALIGN_LEFT = 0
    private const val ALIGN_CENTER = 1
    private const val ALIGN_RIGHT = 2
    private const val DEFAULT_BARCODE_WIDTH = 360
    private const val DEFAULT_BARCODE_HEIGHT = 120

    @Synchronized
    fun setFontPath(path: String) {
        fontPath = path
        if (initialized) {
            // Re-apply the font if printer already initialized
            posApiHelper.PrintSetFontTTF(fontPath, 24.toByte(), 24.toByte())
        }
    }

    @Synchronized
    fun setupPrinter() {
        if (!initialized) {
            posApiHelper.PrintInit()
            posApiHelper.PrintSetFontTTF(fontPath, 24.toByte(), 24.toByte())
            initialized = true
        }
    }

    @Synchronized
    fun setLineSettings(line: PrintLine) {
        posApiHelper.PrintSetBold(if (line.bold) 1 else 0)
        posApiHelper.PrintSetUnderline(if (line.underline) 1 else 0)
    val gray = line.textGray.coerceIn(1, 5)
    posApiHelper.PrintSetGray(gray)
    val alignmentValue = line.alignment ?: ALIGN_LEFT
        posApiHelper.PrintSetAlign(alignmentValue)
    }


    @Synchronized
    fun printLine(line: PrintLine) {
        when (line.type) {
            "TEXT" -> {
                setLineSettings(line)
                posApiHelper.PrintStr(line.text)
            }
            else -> {
                posApiHelper.PrintBarcode(line.text, DEFAULT_BARCODE_WIDTH, DEFAULT_BARCODE_HEIGHT, line.type)
            }
        }
        posApiHelper.PrintStart()
    }

    @Synchronized
    fun printText(text: String) {
        printLine(PrintLine(text, 3, false, false, "TEXT", null))
    }
}