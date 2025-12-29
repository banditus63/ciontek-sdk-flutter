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

                val fontType: Byte
                val multiplier: Byte

                if (line.fontSize >= 32f) {
                fontType = 16.toByte()
                multiplier = 3.toByte()
                } else {
                 fontType = 24.toByte()
                 multiplier = 0.toByte()
             }
                posApiHelper.PrintSetFont(fontType, fontType, multiplier)                   
                posApiHelper.PrintStr(line.text)
            }
            "IMAGE" -> {
                val imageData = line.image
                if (imageData != null) {
                    val alignmentValue = line.alignment ?: ALIGN_LEFT                                
                    val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)

                        if (bitmap != null) {
                            if (alignmentValue == 0) { 
                                posApiHelper.PrintSetLeftIndent(40) 
                        } else {
                        // For CENTER (1) or RIGHT (2), we must have 0 indent for accuracy
                        posApiHelper.PrintSetLeftIndent(0) 
                        }
                        posApiHelper.PrintSetAlign(alignmentValue)                                               
                        posApiHelper.PrintBmp(bitmap)
                        posApiHelper.PrintSetLeftIndent(0) 
                    }
                }
            }
           "QR_CODE" -> {
            val alignmentValue = line.alignment ?: ALIGN_CENTER
    
        
    
            // 2. MINIMIZE Vertical Space
            // PrintSetLineSpace sets the height of the gap between lines.
            // Setting this to 0 (or a very low number like 2) removes the gap above the QR.
            posApiHelper.PrintSetLineSpace(0) 

            posApiHelper.PrintSetAlign(alignmentValue)
    
            // 3. Trim the text to ensure no hidden \n characters are creating space
             val qrData = line.text.trim()
    
            // 4. Print the QR Code
            posApiHelper.PrintBarcode(qrData, 180, 180, "QR_CODE")
    
            // 5. IMPORTANT: Reset line space back to a normal value (e.g., 30-40) 
            // for the text that follows, otherwise the next line of text 
            // will touch the bottom of the QR code.
            posApiHelper.PrintSetLineSpace(0) 
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