package com.imgok.ciontek.models

data class PrintLine(
    val text: String,
    val fontSize: Float,
    val textGray: Int,
    val bold: Boolean,
    val underline: Boolean,
    val type: String,
    val alignment: Int?,
    val image: ByteArray?
) {
    companion object {
        fun fromMap(map: Map<String, Any>): PrintLine {
            return PrintLine(
                text = map["text"] as? String ?: "",
                // Flutter sends numbers as Doubles; convert to Float for Kotlin
                fontSize = (map["fontSize"] as? Double ?: 24.0).toFloat(),
                textGray = map["textGray"] as? Int ?: 3,
                bold = map["bold"] as? Boolean ?: false,
                underline = map["underline"] as? Boolean ?: false,
                type = map["type"] as? String ?: "TEXT",
                alignment = map["alignment"] as? Int,
                image = map["image"] as? ByteArray
            )
        }
    }
}