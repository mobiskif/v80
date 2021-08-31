package ru.mobiskif.jetpack

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun MainMenu(model: MainViewModel) {
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
        /*
        DropdownMenuItem(
            onClick = { dark=!dark; expanded.value = false },
            content = { Text("Dark Theme") }
        )
        */
        DropdownMenuItem(
            onClick = { model.setLightPalette("Фиолетовая"); expanded.value = false },
            content = { Text("Фиолетовая") }
        )
        DropdownMenuItem(
            onClick = { model.setLightPalette("Зеленая"); expanded.value = false },
            content = { Text("Зеленая") }
        )
    }
}
