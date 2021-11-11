package com.sudo.jogingu.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import java.io.File
import java.io.FileOutputStream

fun getDirApp(): String{
    val dir = File(Environment.getDataDirectory(),"Jogingu")
    if(!dir.exists()) dir.mkdir()
    return dir.path
}

fun loadImageFromFile(pathImage: String): Bitmap {
    val image = File(pathImage)
    return BitmapFactory.decodeFile(image.absolutePath)
}

fun saveImageToFile(image: Bitmap?): String{
    val path = getDirApp() + "${System.currentTimeMillis()}.png"
    val outputStream = FileOutputStream(File(path))
    image?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    outputStream.flush()
    outputStream.close()
    return path
}