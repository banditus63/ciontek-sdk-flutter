package com.imgok.ciontek

import com.ctk.sdk.PosApiHelper
import com.imgok.ciontek.models.PrintLine 
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

class PrinterMethodHandler(
    private val posApiHelper: PosApiHelper,
) : MethodCallHandler {

    override fun onMethodCall(call: MethodCall, result: Result) {
        when (call.method) {
            "setFontPath" -> handleSetFontPath(call, result)
            //  "print" -> handlePrint(call, result)
            "print" -> handlePrintLines(call, result)
            else -> result.notImplemented()
        }
    }

    private fun handlePrinterStatus(result: Result): Boolean {
        return when (posApiHelper.PrintCheckStatus()) {
            -1 -> {
                result.error("NO_PAPER_ERROR", "Error, No Paper", null)
                false
            }
            -2 -> {
                result.error("PRINTER_TOO_HOT", "Error, Printer Too Hot", null)
                false
            }
            -3 -> {
                result.error("LOW_BATTERY", "Error, Low Battery", null)
                false
            }
            else -> true
        }
    }

    private fun handlePrint(call: MethodCall, result: Result) {
        if (!handlePrinterStatus(result)) {
            return
        }

        CiontekPrintHelper.setupPrinter()
        val maybeMap = call.arguments as? Map<*, *>
        if (maybeMap == null) {
            result.error("INVALID_ARGUMENT", "Line map is required", null)
            return
        }
        @Suppress("UNCHECKED_CAST")
        val map = maybeMap as Map<String, Any>
        val line = PrintLine.fromMap(map)
        CiontekPrintHelper.printLine(line)
        result.success("Printing")
    }


    private fun handlePrintLines(call: MethodCall, result: Result) {
        // 1. Check printer status
        if (!handlePrinterStatus(result)) { // Assuming this checks for printer availability
            return
        }

        // 2. Retrieve the list of lines from the method call arguments
        val args = call.arguments as? Map<*, *>
        @Suppress("UNCHECKED_CAST")
        val linesMaps = args?.get("lines") as? List<Map<String, Any>>
        
        if (linesMaps == null) {
            result.error("INVALID_ARGUMENT", "Lines list is required", null)
            return
        }

        CiontekPrintHelper.setupPrinter()

        // 3. LOOP: Call the existing printLine helper method for each line
        for (lineMap in linesMaps) {
            val line = PrintLine.fromMap(lineMap) 
            CiontekPrintHelper.printLine(line) // Calls the helper function in your CiontekPrintHelper.kt
        }

        result.success("Printing multiple lines")
    }

    private fun handleSetFontPath(call: MethodCall, result: Result) {
        val path = call.argument<String>("path")
        if (path.isNullOrBlank()) {
            result.error("INVALID_ARGUMENT", "path is required", null)
            return
        }
        CiontekPrintHelper.setFontPath(path)
        result.success(null)
    }
}
