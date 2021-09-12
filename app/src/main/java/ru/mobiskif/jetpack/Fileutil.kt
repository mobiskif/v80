package ru.mobiskif.jetpack

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

fun saveToInternalFolder(context: Context, bitmap: Bitmap, fname: String) {
    try {
        val file = File(context.getExternalFilesDir(null), fname)
        if (!file.exists()) file.createNewFile()
        var fileOutputStream: FileOutputStream? = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        Log.d("jop", "Saved to " + file.absolutePath)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun loadFromInternalFolder(context: Context, fname: String): Bitmap {
    var bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.group_4)
    try {
        val file = File(context.getExternalFilesDir(null), fname)
        if (file.exists()) {
            var fileInputStream: FileInputStream? = FileInputStream(file)
            bitmap = BitmapFactory.decodeStream(fileInputStream)
            Log.d("jop", "Load from " + file.absolutePath)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return bitmap
}
