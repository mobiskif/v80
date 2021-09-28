package ru.mobiskif.jetpack

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue

@ExperimentalMaterialApi
@Composable
fun DBrowser(model: Model) {
    val db = model.repository.db.openHelper.writableDatabase

    val rR = remember { mutableStateOf(TextFieldValue("Distr")) }
    val expanded = remember { mutableStateOf(false) }

    TextButton(onClick = { expanded.value = true }) {
        Text("Таблица: " + rR.value.text)
        Icon(Icons.Default.ArrowDropDown, "")
    }

    var ar: ArrayList<String> = arrayListOf()
    var res = db.query("SELECT * FROM sqlite_master WHERE type = 'table' ORDER BY name")
    res.moveToFirst()
    while (!res.isAfterLast) {
        ar.add(res.getString(1))
        res.moveToNext()
    }
    DropdownMenu(expanded.value, { expanded.value = false }) {
        Column {
            ar.forEach {
                DropdownMenuItem(onClick = {
                    expanded.value = false
                    rR.value = TextFieldValue(it)
                })
                { Text("$it") }
            }
        }
    }

    val res2 = db.query("SELECT * FROM ${rR.value.text}")
    res2.moveToFirst()
    val ar2: ArrayList<String> = arrayListOf()
    while (!res2.isAfterLast) {
        var s = ""
        for (i in 0 until res2.columnCount) {
            s += res2.getString(i) + " "
        }
        ar2.add(s)
        res2.moveToNext()
    }
    LazyColumn {
        items(ar2.size) {
            ListItem(mb) {
                Text("${ar2[it]}")
            }
            Spacer(Modifier.size(space))
        }
    }
}

