package ru.mobiskif.jetpack

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@Composable
fun CurrentInfo(model: MainViewModel) {
    val user = model.cuser.value ?: User(0)

    when (model.getState()) {
        "Изменить пациента" -> { }
        "Инструкция" -> { }
        "Выбрать клинику" -> {
            UsrItems(user = model.cuser.value!!, model = model)
            Text("Чтобы увидеть отложенные талоны, \"войдите\" в поликлинику.", fontSize = small)
            Spacer(Modifier.height(space))
        }
        "Выбрать специальность" -> {
            Column(modFill) {
                Text("${user.F} \n${user.I} ${user.O}", fontWeight = FontWeight.Bold)
                Text("\n${user.Lpu} \nКарточка: ${user.idPat}")
            }
            Spacer(Modifier.height(space))
        }
        "Выбрать врача" -> {
            Column(modFill) {
                Text("${user.F} \n${user.I} ${user.O}", fontWeight = FontWeight.Bold)
                Text("\n${user.Lpu} \n${user.Spec}")
            }
            Spacer(Modifier.height(space))
        }
        "Выбрать талон" -> {
            Column(modFill) {
                Text("${user.F} \n${user.I} ${user.O}", fontWeight = FontWeight.Bold)
                Text("\n${user.Lpu} \n${user.Spec} \n${user.Doc}")
            }
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
fun Fab(model: MainViewModel) {
    if (model.getState() == "Выбрать пациента" || model.getState() == "Инструкция")
        FloatingActionButton(onClick = { model.createUser(); model.setState("Выбрать пациента") }) { Icon(Icons.Filled.Add, "") }
}

@Composable
fun Topbar(model: MainViewModel) {
    TopAppBar(
        title = { Text(model.getState(), maxLines = 1, color = MaterialTheme.colors.error) },
        backgroundColor = MaterialTheme.colors.background,
        //navigationIcon = {  },
        actions = { MainMenu(model) }
    )
}
