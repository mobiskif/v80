package ru.mobiskif.jetpack

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun DistrictSpinner(model: Model, rR: MutableState<TextFieldValue>, irR: MutableState<TextFieldValue>) {
    val dlist = model.distrs.value
    val expanded = remember { mutableStateOf(false) }

    val iconButton =
        Row {
            Text(
                "Район: " + rR.value.text,
                modifier = Modifier.align(Alignment.CenterVertically).clickable { expanded.value = true }
            )
            IconButton(onClick = { expanded.value = true }) {
                Icon(Icons.Default.ArrowDropDown, "District")
            }
        }

    Row {
        iconButton
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            Column {
                dlist?.forEach {
                    DropdownMenuItem(
                        enabled = true,
                        onClick = { expanded.value = false; },
                        content = {
                            Box(
                                Modifier.clickable(onClick = {
                                    expanded.value = false
                                    rR.value = TextFieldValue(it.name.toString())
                                    irR.value = TextFieldValue(it.id)
                                })
                            ) { Text(it.name.toString()) }
                        }
                    )
                }
            }
        }
    }
}
