package ru.mobiskif.jetpack

import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.app.ActivityCompat

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
            onClick = { model.setPalette(context, "Фиолетовая"); expanded.value = false },
            content = { Text("Фиолетовая тема") }
        )
        DropdownMenuItem(
            onClick = { model.setPalette(context, "Зеленая"); expanded.value = false },
            content = { Text("Зеленая тема") }
        )
        DropdownMenuItem(
            onClick = { model.setPalette(context, "Красная"); expanded.value = false },
            content = { Text("Красная тема") }
        )
        Divider()

        DropdownMenuItem(
            onClick = { model.setState("База"); expanded.value = false },
            content = { Text("База") }
        )


        DropdownMenuItem(
            onClick = { },
            content = { Text("v ${context.packageManager.getPackageInfo(context.packageName, 0).versionName}") }
        )

    }
}
