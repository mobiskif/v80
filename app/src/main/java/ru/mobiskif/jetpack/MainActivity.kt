package ru.mobiskif.jetpack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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

var LightPalette = lightColors()
var modFill = Modifier.offset(0.dp,0.dp)
var modBord = Modifier.offset(0.dp,0.dp)
var mod09 = Modifier.offset(0.dp,0.dp)
var modFillVar = Modifier.offset(0.dp,0.dp)

class MainActivity : ComponentActivity() {
    private val model: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()

        model.setContext(applicationContext)
        model.readDistrs()
        model.readUsers()

        model.state.observe(this) { title = it; setContent { MainView(model) } }
        model.users.observe(this) { if (it.isEmpty()) model.setState("Инструкция"); setContent { MainView(model) } }
        model.cuser.observe(this) {
            if (it.idPat!!.isNotEmpty()) model.readHists(it)
            setContent { MainView(model) } }
        model.lpus.observe(this) {
            model.readHistsAll(model.cuser.value!!)
            setContent { MainView(model) }
        }
        model.specs.observe(this) { setContent { MainView(model) } }
        model.wait.observe(this) { setContent { MainView(model) } }
        model.docs.observe(this) { setContent { MainView(model) } }
        model.talons.observe(this) { setContent { MainView(model) } }
        model.history.observe(this) { setContent { MainView(model) } }
        model.idtalon.observe(this) {
            model.readHists(model.cuser.value!!)
            model.readHistsAll(model.cuser.value!!)
            model.setState("Выбрать специальность")
            setContent { MainView(model) }
        }
    }

    override fun onBackPressed() {
        var state = model.getState()
        when (state) {
            "Инструкция" -> state = "Выбрать пациента"
            "Изменить пациента" -> state = "Выбрать пациента"
            "Выбрать пациента" -> {}
            "Выбрать клинику" -> state = "Выбрать пациента"
            "Выбрать специальность" -> state = "Выбрать клинику"
            "Выбрать врача" -> state = "Выбрать специальность"
            "Выбрать талон" -> state = "Выбрать врача"
            "Отменить талон" -> state = "Выбрать специальность"
            "Взять талон" -> state = "Выбрать специальность"
        }
        model.setState(state)
    }
}

@Composable
fun MainView(model: MainViewModel) {
    val users = model.users.value ?: listOf()
    val lpus = model.lpus.value ?: listOf()
    val specs = model.specs.value ?: listOf()
    val docs = model.docs.value ?: listOf()
    val talons = model.talons.value ?: listOf()
    val hists = model.history.value ?: listOf()
    val histsall = model.historyall.value ?: listOf()

    MainTheme {
        fixModes()
        Scaffold(floatingActionButton = { Fab(model) }, topBar = { Topbar(model) }) {
            Column(Modifier.padding(space)) {
                CurrentInfo(model)
                if (model.wait.value == true)
                    CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
                else {
                    when (model.getState()) {
                        "Изменить пациента" -> LazyColumn { items(1) { UsrItemsEdit(model.cuser.value!!, model) } }
                        "Выбрать пациента" -> LazyColumn { items(users.size) { UsrItems(users[it], model) } }
                        "Выбрать клинику" -> {
                            LazyRow { items(histsall.size) { HistItems(histsall[it], model) } }
                            Spacer(Modifier.size(space))
                            LazyColumn { items(lpus.size) { LpuItems(lpus[it], model) } }
                        }
                        "Выбрать специальность" -> {
                            LazyRow { items(hists.size) { HistItems(hists[it], model) } }
                            Spacer(Modifier.size(space))
                            LazyColumn { items(specs.size) { SpecItems(specs[it], model) } }
                        }
                        "Выбрать врача" -> LazyColumn { items(docs.size) { DocItems(docs[it], model) } }
                        "Выбрать талон" -> LazyColumn { items(talons.size) { TalonItems(talons[it], model) } }
                        "Взять талон" -> LazyColumn { items(1) { TalonTake(model) } }
                        "Отменить талон" -> LazyColumn { items(1) { TalonBrake(model) } }
                    }
                }
            }
        }
    }
}

