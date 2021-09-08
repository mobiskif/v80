package ru.mobiskif.jetpack

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.os.Environment
import androidx.compose.material.Text
import java.io.File

var LightPalette = lightColors()
var modFill = Modifier.offset(0.dp,0.dp)
var modBord = Modifier.offset(0.dp,0.dp)
var mod09 = Modifier.offset(0.dp,0.dp)
var modFillVar = Modifier.offset(0.dp,0.dp)

class MainActivity : ComponentActivity() {
    private val model: Model by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()

        model.setDBContext(applicationContext)
        model.setPalette(this, "Фиолетовая")
        model.readDistrs()
        model.readUsers()

        model.state.observe(this) { title = it; setContent { MainView(this, model) } }
        model.users.observe(this) { if (it.isEmpty()) model.setState("Инструкция"); setContent { MainView(this, model) } }
        model.cuser.observe(this) {
            if (it.idPat!!.isNotEmpty()) model.readHists(it)
            setContent { MainView(this, model) } }
        model.lpus.observe(this) { setContent { MainView(this, model) } }
        model.specs.observe(this) { setContent { MainView(this, model) } }
        model.wait.observe(this) { setContent { MainView(this, model) } }
        model.docs.observe(this) { setContent { MainView(this, model) } }
        model.talons.observe(this) { setContent { MainView(this, model) } }
        model.history.observe(this) { setContent { MainView(this, model) } }
        model.idtalon.observe(this) { Toast.makeText(this,it,Toast.LENGTH_LONG).show() }
    }

    override fun onBackPressed() {
        var state = model.getState()
        when (state) {
            "Инструкция" -> state = "Выбрать пациента"
            "Выбрать пациента" -> state = "Инструкция"
            "Изменить пациента" -> state = "Выбрать пациента"
            "Выбрать клинику" -> state = "Выбрать пациента"
            "Выбрать специальность" -> state = "Выбрать клинику"
            "Выбрать врача" -> state = "Выбрать специальность"
            "Выбрать талон" -> state = "Выбрать врача"
            "Отменить талон" -> state = "Выбрать специальность"
            "Взять талон" -> state = "Выбрать талон"
        }
        model.setState(state)
    }
}

@Composable
fun MainView(context: Context, model: Model) {
    val users = model.users.value ?: listOf()
    val lpus = model.lpus.value ?: listOf()
    val specs = model.specs.value ?: listOf()
    val docs = model.docs.value ?: listOf()
    val talons = model.talons.value ?: listOf()
    val hists = model.history.value ?: listOf()

    Theme {
        FixModes()
        Scaffold(floatingActionButton = { Fab(model) }, topBar = { Topbar(context, model) }) {
            Column(Modifier.padding(space)) {
                CurrentInfo(model)
                if (model.wait.value == true)
                    CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
                else {
                    when (model.getState()) {
                        "Инструкция" -> LazyColumn { items(1) { Help() } }
                        "Изменить пациента" -> LazyColumn { items(1) { UsrItemsEdit(model.cuser.value!!, model) } }
                        "Выбрать пациента" -> LazyColumn { items(users.size) { UsrItems(users[it], model) } }
                        "Выбрать клинику" -> LazyColumn { items(lpus.size) { LpuItems(lpus[it], model) } }
                        "Выбрать специальность" -> {
                            if (hists.isNotEmpty()) {
                                Text("Отложенные талоны:")
                                LazyRow { items(hists.size) { HistItems(hists[it], model) } }
                                Spacer(Modifier.size(space))
                            }
                            LazyColumn { items(specs.size) { SpecItems(specs[it], model) } }
                        }
                        "Выбрать врача" -> LazyColumn { items(docs.size) { DocItems(docs[it], model) } }
                        "Выбрать талон" -> LazyColumn { items(talons.size) { TalonItems(talons[it], model) } }
                        "Взять талон" -> LazyColumn { items(1) { TalonTake(model) } }
                        "Отменить талон" -> LazyColumn { items(1) { TalonTake(model) } }
                        //"Выбрать фото" -> LazyColumn { items(1) { DialogComponent(context, model) } }
                    }
                }
            }
        }
    }
}

