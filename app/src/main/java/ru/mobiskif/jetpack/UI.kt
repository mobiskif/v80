package ru.mobiskif.jetpack

import android.content.Context
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.LiveData
import java.io.File

@Composable
fun CurrentInfo(model: Model) {
    val user = model.cuser.value ?: User(0)

    when (model.getState()) {
        "Изменить пациента" -> {
        }
        "Инструкция" -> {
        }
        "Выбрать клинику" -> {
            UsrItems(user, model)
            Text("Чтобы увидеть отложенные талоны, \"войдите\" в поликлинику.", fontSize = small)
            Spacer(Modifier.height(space))
        }
        "Выбрать специальность" -> {
            UsrItems(user, model)
            Text("${user.Lpu} \nКарточка: ${user.idPat}", fontSize = small)
            /*
            Column(modFill) {
                Text("${user.F} \n${user.I} ${user.O}", fontWeight = FontWeight.Bold)
                Text("\n${user.Lpu} \nКарточка: ${user.idPat}")
            }

             */
            Spacer(Modifier.height(space))
        }
        "Выбрать врача" -> {
            UsrItems(user, model)
            Text("${user.Lpu} \n${user.Spec}", fontSize = small)
            /*
            Column(modFill) {
                Text("${user.F} \n${user.I} ${user.O}", fontWeight = FontWeight.Bold)
                Text("\n${user.Lpu} \n${user.Spec}")
            }

             */
            Spacer(Modifier.height(space))
        }
        "Выбрать талон" -> {
            UsrItems(user, model)
            Text("${user.Lpu} \n${user.Spec} \n${user.Doc}", fontSize = small)
            /*
            Column(modFill) {
                Text("${user.F} \n${user.I} ${user.O}", fontWeight = FontWeight.Bold)
                Text("\n${user.Lpu} \n${user.Spec} \n${user.Doc}")
            }

             */
            Spacer(Modifier.height(space))
        }
    }
}

@Composable
fun Help() {
    Text("Добавляйте пациентов, используя кнопку \"Плюс\" внизу справа.", modifier = modFill)
    Spacer(Modifier.size(space))
    Text("Нажав на символ \"Карандаш\", отредактируйте персональные данные.", modifier = modBord)
    Spacer(Modifier.size(space))
    Text("Буквы в именах пишите так, как записано в регистратуре (\"ё\", \"-оглы\" и т.п.)", modifier = modFill)
    Spacer(Modifier.size(space))
    Text("Дату укажите в формате \"1984-09-23\", район выберите из списка.", modifier = modBord)
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

@Composable
fun DialogComponent(context: Context, model: Model) { //path: String = Environment.getExternalStorageDirectory().path) {
    //val path: String = Environment.getExternalStorageDirectory().path
    val path: String = model.cuser.value?.Photo.toString()
    val flist = File(path).listFiles()
    Log.d("jop","=== $path")

    if (flist.isNotEmpty()) {
        flist.forEach {
            Text("${it.name}", Modifier.clickable {
                var u = model.cuser.value!!
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
