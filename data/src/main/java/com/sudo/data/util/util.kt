package com.sudo.data.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.widget.ImageView
import com.sudo.data.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.Date
import java.util.Calendar
import java.util.UUID

fun genId(prefix: String = ""): String{
    return prefix + UUID.randomUUID()
}

fun calculateAge(date: Date): Int{
    val now = Calendar.getInstance().get(Calendar.YEAR)
    val born = Calendar.getInstance()
    born.time = date
    return now - born.get(Calendar.YEAR)
}

fun getDirApp(): String{
    val dir = File(Environment.getDataDirectory(),"Jogingu")
    if(!dir.exists()) dir.mkdir()
    return dir.path
}

//fun loadImageFromFile(pathImage: String): ByteArray{
//    val image = File(pathImage)
//    return BitmapFactory.decodeFile(image.absolutePath)
//}
//
//fun saveImageToFile(image: Bitmap?): String{
//    val path = getDirApp() + "${System.currentTimeMillis()}.png"
//    val outputStream = FileOutputStream(File(path))
//    image?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
//    outputStream.flush()
//    outputStream.close()
//    return path
//}

