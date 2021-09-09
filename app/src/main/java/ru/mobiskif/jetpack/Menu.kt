package ru.mobiskif.jetpack

import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun Menu(context: Context, model: Model) {
    val expanded = remember { mutableStateOf(false) }
    IconButton(
        onClick = { expanded.value = true },
        content = { Icon(Icons.Default.MoreVert, "Menu") }
    )
    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false }
    ) {
        DropdownMenuItem(
            onClick = { model.setState("Инструкция"); expanded.value = false },
            content = { Text("Инструкция") }
        )
        DropdownMenuItem(
            onClick = { model.setState("Выбрать пациента"); expanded.value = false },
            content = { Text("Выбрать пациента") }
        )
        Divider()
        DropdownMenuItem(
            onClick = { val st = model.getState(); model.setState("Инструкция"); model.setPalette(context, "Фиолетовая"); expanded.value = false; model.setState(st) },
            content = { Text("Фиолетовая тема") }
        )
        DropdownMenuItem(
            onClick = { val st = model.getState(); model.setState("Инструкция"); model.setPalette(context, "Зеленая"); expanded.value = false; model.setState(st) },
            content = { Text("Зеленая тема") }
        )
        DropdownMenuItem(
            onClick = { },
            content = { Text("Версия ${context.packageManager.getPackageInfo(context.packageName, 0).versionName}") }
        )
    }
}
