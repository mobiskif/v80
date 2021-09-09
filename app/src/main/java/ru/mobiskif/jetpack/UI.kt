package ru.mobiskif.jetpack

import android.content.Context
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.io.File

@Composable
fun CurrentInfo(model: Model) {
    val user = model.cuser.value ?: User(0)
    when (model.getState()) {
        "Выбрать клинику" -> {
            UsrItems(user, model)
            Text("Чтобы увидеть отложенные талоны, \"войдите\" в поликлинику.", fontSize = small)
            Spacer(Modifier.height(space))
        }
        "Выбрать специальность" -> {
            UsrItems(user, model)

            val lpu = Lpu("0")
            lpu.name = user.Lpu
            lpu.lid = user.iLpu.toString()
            lpu.did = user.iDistr.toString()
            LpuItems(lpu = lpu, model = model)
        }
        "Выбрать врача" -> {
            UsrItems(user, model)

            val lpu = Lpu("0")
            lpu.name = user.Lpu
            lpu.lid = user.iLpu.toString()
            lpu.did = user.iDistr.toString()
            LpuItems(lpu = lpu, model = model)

            val spec = Spec("0")
            spec.name = user.Spec
            spec.free = user.FreeSpec
            spec.lpu = user.Lpu
            spec.id = user.iSpec.toString()
            SpecItems(spec = spec, model = model)

        }
        "Выбрать талон" -> {
            UsrItems(user, model)

            val lpu = Lpu("0")
            lpu.name = user.Lpu
            lpu.lid = user.iLpu.toString()
            lpu.did = user.iDistr.toString()
            LpuItems(lpu = lpu, model = model)

            val doc = Doc("0")
            doc.name= user.Doc
            doc.free=user.FreeDoc
            doc.id= user.iDoc.toString()
            DocItems(doc = doc, model = model)

        }

        "Взять талон", "Отменить талон" -> {
            UsrItems(user, model)

            val lpu = Lpu("0")
            lpu.name = user.Lpu
            lpu.lid = user.iLpu.toString()
            lpu.did = user.iDistr.toString()
            LpuItems(lpu = lpu, model = model)
        }
        else -> {}
    }
}

@Composable
fun Help() {
    Text("Добавляйте пациентов, используя кнопку \"Плюс\" внизу справа.", modifier = modFill)
    Spacer(Modifier.size(space))
    Text("Нажав на символ \"Портрет\", отредактируйте персональные данные.", modifier = modBord)
    Spacer(Modifier.size(space))
    Text("Буквы в именах пишите так, как записано в регистратуре (\"ё\", \"-оглы\" и т.п.)", modifier = modFill)
    Spacer(Modifier.size(space))
    Text("Дату укажите в формате \"1984-09-23\", район выберите из списка.", modifier = modBord)
    Spacer(Modifier.size(space))
    Text("Нажав на \"ФИО\", получите список доступных поликлиник.", modifier = modFill)
    Spacer(Modifier.size(space))
    Text("Список доступных поликлиник определн вашим полисом ОМС", modifier = modBord)
    Spacer(Modifier.size(space))
    Text("Ненужные клиники удалите нажав на символ \"Корзина\". Список заново восстановится, если удалить всё.", modifier = modFill)
    Spacer(Modifier.size(space))
    Text("Приложение транслирует ответы регистратур \"как есть\". Прикреплением, расписанием и доступностью талонов разработчик не управляет.", modifier = modBord)
    Spacer(Modifier.size(space))
    Text("Отменяйте ненужные талоны: Выбрать пациента -> Выбрать клинику - увидите отложенные талоны (если есть). Нажатие на талон - отмена.", modifier = modFill)
}

@Composable
fun Fab(model: Model) {
    if (model.getState() == "Выбрать пациента" || model.getState() == "Инструкция")
        FloatingActionButton(onClick = { model.createUser(); model.setState("Выбрать пациента") }) { Icon(Icons.Filled.Add, "") }
}

@Composable
fun Topbar(context: Context, model: Model) {
    TopAppBar(
        title = { Text(model.getState(), maxLines = 1, color = MaterialTheme.colors.error) },
        backgroundColor = MaterialTheme.colors.background,
        //navigationIcon = {  },
        actions = { Menu(context, model) }
    )
}

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun DialogComponent(context: Context, model: Model) { //path: String = Environment.getExternalStorageDirectory().path) {
    val path: String = Environment.getStorageDirectory().path
    //path = "/storage/emulated/0"
    //val path: String = model.cuser.value?.Photo.toString()
    val flist = File(path).listFiles()
    Log.d("jop","=== $path")

    if (!flist.isNullOrEmpty()) {
        flist.forEach {
            Text("${it.name}", Modifier.clickable {
                val u = model.cuser.value!!
                u.Photo = it.path
                model.setCurrentUser(u)
            })
            Log.d("jop","$it")
        }
    }

    /*
    val openDialog = remember { mutableStateOf(true) }
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text("Выбери файл") },
            text = {
                Column {
                    flist.forEach {
                        TextButton(onClick = {
                            Toast.makeText(context, "$it", Toast.LENGTH_LONG).show()
                        })
                        { Text("${it.canonicalPath}") }
                    }
                }
            },
            confirmButton = { },
            dismissButton = {
                TextButton(onClick = { openDialog.value = false })
                { Text("Отмена") }
            }
        )
    }
     */
}
