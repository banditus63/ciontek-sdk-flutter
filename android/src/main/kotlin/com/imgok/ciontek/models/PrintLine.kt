package com.imgok.ciontek
import com.imgok.ciontek.models.PrintLine

class PrintLine(
    val text: String,
    val fontSize: Float,    
    val textGray: Int,      
    val bold: Boolean,      
    val underline: Boolean, 
    val type: String,       
    val alignment: Int?,    
    val image: ByteArray?   
){
    companion object {
        fun fromMap(map: Map<String, Any>): PrintLine {
    return PrintLine(
        text = map["text"] as? String ?: "",
        // !!! THIS IS THE IMPORTANT PART !!!
        // Flutter sends numbers as Doubles, so we cast to Double then to Float
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
