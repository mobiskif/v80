package ru.mobiskif.jetpack

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

var LightPalette = lightColors()
var modFill = Modifier.offset(0.dp, 0.dp)
var modBord = Modifier.offset(0.dp, 0.dp)
var mod09 = Modifier.offset(0.dp, 0.dp)
var modFillVar = Modifier.offset(0.dp, 0.dp)


class MainActivity : ComponentActivity() {
    private val model: Model by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()

        model.setDBContext(applicationContext)
        model.setPalette(this, "Фиолетовая")
        model.readDistrs()
        model.readUsers()

        model.state.observe(this) {
            title = it;
            setContent { MainView(this, model) }
        }
        model.users.observe(this) {
            if (it.isEmpty()) model.setState("Инструкция")
            setContent { MainView(this, model) }
        }
        model.cuser.observe(this) {
            model.setPalette(this, it.Palette.toString(), it)
            if (it.idPat!!.isNotEmpty()) model.readHists(it)
            setContent { MainView(this, model) }
        }
        model.lpus.observe(this) { setContent { MainView(this, model) } }
        model.specs.observe(this) { setContent { MainView(this, model) } }
        model.wait.observe(this) { setContent { MainView(this, model) } }
        model.docs.observe(this) { setContent { MainView(this, model) } }
        model.talons.observe(this) { setContent { MainView(this, model) } }
        model.history.observe(this) { setContent { MainView(this, model) } }
        model.idtalon.observe(this) { Toast.makeText(this, it, Toast.LENGTH_LONG).show() }
    }

    override fun onBackPressed() {
        var state = model.getState()
        when (state) {
            "Инструкция", "Изменить пациента", "Выбрать клинику" -> state = "Выбрать пациента"
            "Выбрать пациента" -> state = "Инструкция"
            //"Изменить пациента" -> state = "Выбрать пациента"
            //"Выбрать клинику" -> state = "Выбрать пациента"
            "Выбрать специальность" -> state = "Выбрать клинику"
            "Выбрать врача" -> state = "Выбрать специальность"
            "Выбрать талон" -> state = "Выбрать врача"
            "Отменить талон" -> state = "Выбрать специальность"
            "Взять талон" -> state = "Выбрать талон"
        }
        model.setState(state)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 777 && data != null) {
            val bm = data.extras?.get("data") as Bitmap
            //val fname = data.extras?.get("fname") as String
            val fname = "${model.cuser.value?.id}.png"
            saveToInternalFolder(this, bm, fname)
        }
    }

}

@Composable
fun MainView(activity: Activity, model: Model) {
    val users = model.users.value ?: listOf()
    val lpus = model.lpus.value ?: listOf()
    val specs = model.specs.value ?: listOf()
    val docs = model.docs.value ?: listOf()
    val talons = model.talons.value ?: listOf()
    val hists = model.history.value ?: listOf()

    Theme {
        FixModes()
        Scaffold(floatingActionButton = { Fab(model) }, topBar = { Topbar(activity, model) }) {
            Column(Modifier.padding(space)) {
                CurrentInfo(activity, model)
                if (model.wait.value == true) CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
                else {
                    when (model.getState()) {
                        "Инструкция" -> LazyColumn { items(1) { Help() } }
                        "Изменить пациента" -> LazyColumn { items(1) { UsrDataEdit(activity, model.cuser.value!!, model) } }
                        "Выбрать пациента" -> LazyColumn { items(users.size) { UsrItemsView(activity, users[it], model) } }
                        "Выбрать клинику" -> LazyColumn { items(lpus.size) { LpuItems(lpus[it], model) } }
                        "Выбрать специальность" -> {
                            if (hists.isNotEmpty()) {
                                Text("Отложено:")
                                LazyRow { items(hists.size) { HistItems(hists[it], model) } }
                                Spacer(Modifier.size(space))
                            }
                            LazyColumn { items(specs.size) { SpecItems(specs[it], model) } }
                        }
                        "Выбрать врача" -> LazyColumn { items(docs.size) { DocItems(docs[it], model) } }
                        "Выбрать талон" -> LazyColumn { items(talons.size) { TalonItems(talons[it], model) } }
                        "Взять талон" -> LazyColumn { items(1) { TalonTake(model) } }
                        "Отменить талон" -> LazyColumn { items(1) { TalonTake(model) } }
                    }
                }
            }
        }
    }
}


