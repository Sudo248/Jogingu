package com.sudo248.data.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.widget.ImageView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sudo248.data.R
import com.sudo248.data.local.database.models.RunDB
import com.sudo248.domain.entities.Run
import com.sudo248.domain.entities.RunInStatistic
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

fun RunDB.toRunInStatistic(): RunInStatistic {
    return RunInStatistic(
        runId = this.runId,
        timeRunning = this.timeRunning,
        caloBurned = this.caloBurned,
        day = this.timeStart,
        distance = this.distance,
        stepCount = this.stepCount
    )
}

inline fun <reified T> convertToString(item: T): String {
    val type = object : TypeToken<T>() {}.type
    return Gson().toJson(item, type)
}

inline fun <reified T> convertFromString(item: String): T {
    val type = object : TypeToken<T>() {}.type
    return Gson().fromJson<T>(item, type)
}


