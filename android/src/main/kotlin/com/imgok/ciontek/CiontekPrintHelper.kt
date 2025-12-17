package com.imgok.ciontek

import com.ctk.sdk.PosApiHelper
import com.imgok.ciontek.models.PrintLine
import android.graphics.Bitmap
import android.graphics.BitmapFactory

object CiontekPrintHelper {
    private val posApiHelper: PosApiHelper = PosApiHelper.getInstance()

   

    @Volatile
    private var initialized: Boolean = false

    private const val ALIGN_LEFT = 0
    private const val ALIGN_CENTER = 1
    private const val ALIGN_RIGHT = 2
    private const val DEFAULT_BARCODE_WIDTH = 360
    private const val DEFAULT_BARCODE_HEIGHT = 120

   

    @Synchronized
    fun setupPrinter() {
        if (!initialized) {
            posApiHelper.PrintInit()
            posApiHelper.PrintSetFont(0.toByte(), 0.toByte(), 0.toByte())
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

                val multiplier: Byte = when {
                line.fontSize >= 48f -> 2.toByte() // Huge (Triple size)
                line.fontSize >= 32f -> 1.toByte() // Large (Double size)
                else -> 0.toByte()                // Normal (Standard size)
                }
                posApiHelper.PrintSetFont(0.toByte(), multiplier, multiplier)                   
                posApiHelper.PrintStr(line.text)
            }
            "IMAGE" -> {
                val imageData = line.image
                if (imageData != null) {
                    val alignmentValue = line.alignment ?: ALIGN_CENTER
                                
                    val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
                    if (bitmap != null) {
                        posApiHelper.PrintSetAlign(alignmentValue)                        
                        posApiHelper.PrintBmp(bitmap) 
                    }
                }
            }
            "QR_CODE" -> {
            val alignmentValue = line.alignment ?: ALIGN_CENTER
            posApiHelper.PrintSetAlign(alignmentValue)
            
            // Use PrintBarcode for QR codes. 
            // Type 58 is commonly used for QR_CODE in this SDK, 
            // but we can pass the string "QR_CODE" if the SDK supports it.
            posApiHelper.PrintBarcode(line.text, 180, 180, "QR_CODE")
            }
            else -> {
                posApiHelper.PrintBarcode(line.text, DEFAULT_BARCODE_WIDTH, DEFAULT_BARCODE_HEIGHT, line.type)
            }
        }
        posApiHelper.PrintStart()
        
    }   

    @Synchronized
    fun printText(text: String) {
        // Updated to pass null for the new image parameter
        printLine(PrintLine(text, 24f, 3, false, false, "TEXT", null, null))
    }
}