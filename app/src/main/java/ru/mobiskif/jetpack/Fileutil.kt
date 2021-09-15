package ru.mobiskif.jetpack

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.ui.res.painterResource
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

private const val PERMISSION_READ_EXTERNAL_STORAGE = 5

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

//Check if you already have read storage permission
fun checkPermissionForReadWrite(context: Context): Boolean {
    val result: Int =
        ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
    Log.d("jop","=== check permission $result")
    return result == PackageManager.PERMISSION_GRANTED
}

fun checkPermissionForCamera(context: Context): Boolean {
    val result: Int =
        ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.CAMERA
        )
    Log.d("jop","=== check permission $result")
    return result == PackageManager.PERMISSION_GRANTED
}

//Request Permission For Read Storage
fun requestPermissionForReadWrite(context: Context) {
    Log.d("jop","--- request permission")
    ActivityCompat.requestPermissions(
        context as Activity,
        arrayOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ), PERMISSION_READ_EXTERNAL_STORAGE
    )
}

//Request Permission For Read Storage
fun requestPermissionForCamera(context: Context) {
    Log.d("jop","--- request permission")
    ActivityCompat.requestPermissions(
        context as Activity,
        arrayOf(
            android.Manifest.permission.CAMERA
        ), PERMISSION_READ_EXTERNAL_STORAGE
    )
}

