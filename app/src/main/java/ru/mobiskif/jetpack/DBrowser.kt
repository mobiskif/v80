package ru.mobiskif.jetpack

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun DBrowser(model: Model) {
    var table = "Distr"
    val db = model.repository.db.openHelper.writableDatabase
    var ar: ArrayList<String> = arrayListOf()
    var res = db.query("SELECT * FROM sqlite_master WHERE type = 'table' ORDER BY name")

    res.moveToFirst()
    while (!res.isAfterLast) {
        ar.add(res.getString(1))
        res.moveToNext()
    }

    LazyRow() {
        items(ar.size) {
            //ListItem {
                Text("${ar[it]}", modifier = mfp.clickable {
                    model.repository.setWait(true)
                    //db.delete("sqlite_master", "name = $name", null)
                    table = ar[it]
                    //model.repository.setWait(false)
                })
            //}
            Spacer(Modifier.size(space))
        }
    }

    val res2 = db.query("SELECT * FROM $table")
    res2.moveToFirst()
    val ar2: ArrayList<String> = arrayListOf()
    while (!res2.isAfterLast) {
        ar2.add(res2.getString(1))
        res2.moveToNext()
    }

    Spacer(Modifier.size(space))
    LazyColumn {
        items(ar2.size) {
            ListItem(mb) {
                Text("${ar2[it]}")
            }
            Spacer(Modifier.size(space))
        }
    }
}

