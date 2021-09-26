package ru.mobiskif.jetpack

import android.app.Activity
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {
    private val model: Model by viewModels()

    //@RequiresApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()

        model.setDBContext(applicationContext)
        model.readDistrs()
        //model.readLpusFull()
        //model.readUsers()

        model.state.observe(this) {
            title = it
            setContent { MainView(this, model) }
        }
        model.users.observe(this) {
            if (it.isEmpty()) model.setState("Инструкция")
            setContent { MainView(this, model) }
        }
        model.cuser.observe(this) {
            if (!it.idPat.isNullOrEmpty()) model.readHists(it)
            setContent { MainView(this, model) }
        }
        model.lpus.observe(this) { setContent { MainView(this, model) } }
        model.specs.observe(this) { setContent { MainView(this, model) } }
        model.docs.observe(this) { setContent { MainView(this, model) } }
        model.talons.observe(this) { setContent { MainView(this, model) } }
        model.history.observe(this) { setContent { MainView(this, model) } }
        model.idtalon.observe(this) { Toast.makeText(this, it, Toast.LENGTH_LONG).show() }
        model.confs.observe(this) { confs ->
            confs.forEach {
                when (it.name) {
                    "palette" -> model.setPalette(this, it.value)
                }
            }
            setContent { MainView(this, model) }
        }
        model.wait.observe(this) {
            Log.d("jop",it.toString())
            setContent { MainView(this, model) }
        }
        model.palette.observe(this) { setContent { MainView(this, model) } }
    }

    override fun onPause() {
        super.onPause()
        model.writeConfs()
    }

    override fun onResume() {
        super.onResume()
        model.readConfs()
    }

    override fun onBackPressed() {
        var state = model.getState()
        when (state) {
            "Выбрать пациента" -> state = "Инструкция"
            "Инструкция", "Изменить пациента", "Выбрать клинику" -> state = "Выбрать пациента"
            "Выбрать специальность" -> state = "Выбрать клинику"
            "Выбрать врача" -> state = "Выбрать специальность"
            "Выбрать талон" -> state = "Выбрать врача"
            "Отменить талон" -> state = "Выбрать специальность"
            "Взять талон" -> state = "Выбрать талон"
        }
        model.setState(state)
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val bm = data?.extras?.get("data") as Bitmap
            //val fname2 = data.extras?.get("fname") as String - не работает
            val fname = "${model.cuser.value?.id}.png"
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
                if (!checkPermissionForReadWrite(this)) requestPermissionForReadWrite(this)
            saveToInternalFolder(this, bm, fname)
        }
        model.repaint()
    }

}


@ExperimentalMaterialApi
@Composable
fun MainView(activity: MainActivity, model: Model) {
    val users = model.users.value ?: listOf()
    val lpus = model.lpus.value ?: listOf()
    val specs = model.specs.value ?: listOf()
    val docs = model.docs.value ?: listOf()
    val talons = model.talons.value ?: listOf()
    val hists = model.history.value ?: listOf()

    Theme {
        DefineModes()
        Scaffold(
            floatingActionButton = { Fab(model) },
            topBar = { Topbar(activity, model) },
        ) {
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
                        "Поликлиника" -> LazyColumn { items(1) { LpuInfoDialog(model) } }
                        "База" -> DBrowser(model)
                    }
                }
            }
        }
    }
}
