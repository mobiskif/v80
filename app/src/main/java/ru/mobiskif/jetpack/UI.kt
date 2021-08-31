package ru.mobiskif.jetpack

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CurrentInfo(model: MainViewModel) {
    val user = model.cuser.value ?: User(0, "", "", "", "", "1", "", "", "", "","","","")
    val mod0 = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colors.secondary, RoundedCornerShape(space))
        .padding(space)
    val mod1 = Modifier
        .fillMaxWidth()
        .border(1.dp, MaterialTheme.colors.secondary, RoundedCornerShape(space))
        .padding(space)

    when (model.getState()) {
        "Изменить пациента" -> {}
        "Инструкция" -> {
            //if (model.users.value?.size==0) {
                Column () {
                    Text("Добавляйте пациентов, используя кнопку \"Плюс\" внизу справа.", modifier = mod0)
                    Spacer(Modifier.size(space))
                    Text("Нажав на символ \"Карандаш\", отредактируйте персональные данные.", modifier = mod1)
                    Spacer(Modifier.size(space))
                    Text("Буквы в именах пишите так, как записано в регистратуре (\"ё\", \"-оглы\" и т.п.)", modifier = mod0)
                    Spacer(Modifier.size(space))
                    Text("Дату укажите в формате \"1984-09-23\", район выберите из списка.", modifier = mod1)
                    Spacer(Modifier.size(space))
                    Text("Ненужные клиники удалите нажав на символ \"Корзина\". Список заново восстановится, если удалить всё.", modifier = mod0)
                    Spacer(Modifier.size(space))
                    Text("Приложение транслирует ответы регистратур клиник без изменений. Прикреплением, расписанием и доступностью талонов разработчик не управляет.", modifier = mod1)
                }
                Spacer(Modifier.height(space))
            //}
        }
        "Выбрать клинику" -> {
            Column(mod0) {
                Text("${user.F} \n${user.I} ${user.O}", fontWeight = FontWeight.Bold)
                Text("\n${user.D} \n${user.Distr} район",)
            }
            Spacer(Modifier.height(space))
        }
        "Выбрать специальность" -> {
            Column(mod0) {
                Text("${user.F} \n${user.I} ${user.O}", fontWeight = FontWeight.Bold)
                Text("\n${user.Lpu} \nКарточка: ${user.idPat}",)
            }
            Spacer(Modifier.height(space))
        }
        "Выбрать врача" -> {
            Column(mod0) {
                Text("${user.F} \n${user.I} ${user.O}", fontWeight = FontWeight.Bold)
                Text("\n${user.Lpu} \n${user.Spec}",)
            }
            Spacer(Modifier.height(space))
        }
        "Выбрать талон" -> {
            Column(mod0) {
                Text("${user.F} \n${user.I} ${user.O}", fontWeight = FontWeight.Bold)
                Text("\n${user.Lpu} \n${user.Spec} \n${user.Doc}",)
            }
            Spacer(Modifier.height(space))
        }
    }
}

@Composable
fun Fab(model: MainViewModel) {
    if (model.getState() == "Выбрать пациента" || model.getState() == "Инструкция")
        FloatingActionButton(onClick = { model.createUser(); model.setState("Выбрать пациента") }) { Icon(Icons.Filled.Add,"") }
}

@Composable
fun Topbar(model: MainViewModel) {
    TopAppBar(
        title = { Text(model.getState(), maxLines = 1, color = MaterialTheme.colors.error) },
        backgroundColor = MaterialTheme.colors.background,
        //navigationIcon = {  },
        //actions = { IconButton(onClick = { }) { Icon(Icons.Filled.Menu, "")} },
        actions = { MainMenu(model) }
    )
}

@Preview( showBackground = true, backgroundColor = 0xFFFFFF, uiMode = Configuration.UI_MODE_NIGHT_NO, showSystemUi = false)
@Composable
fun UsrItemsPreview() {

    val mod0 = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colors.secondary, RoundedCornerShape(space))
        .padding(space)

    val mod1 = Modifier.fillMaxWidth(.9f)

    Column(Modifier.padding(space)) {
        Button({}) { Text("Button") }
        Switch(checked = true, onCheckedChange = {})
        RadioButton(selected = true, onClick = { })
        FloatingActionButton (onClick = {}) { Icon(Icons.Filled.Edit,"",tint = MaterialTheme.colors.error)}
        Spacer(Modifier.height(space))
        for (i in 0..3) {
            Row(mod0) {
                Column(mod1) { Text("adfsdfsdfsd 3245345324 dfghdfghd f df567346456 tyeryer dfsdwertywewtrt dfgsdfgsdfgsdfgssdfgsd fgsdfg sdsfg sdfg") }
                Column { Icon(Icons.Filled.Edit, "", Modifier.alpha(.33f)) }
            }
            Spacer(Modifier.height(space))
        }
    }
}

