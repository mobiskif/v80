package ru.mobiskif.jetpack

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@ExperimentalMaterialApi
@Composable
fun CurrentInfo(activity: Activity, model: Model) {
    val user = model.cuser.value ?: User(0)
    when (model.getState()) {
        "Выбрать клинику" -> {
            UsrItemsView(activity, user, model)
            Text("Чтобы увидеть отложенные талоны, \"войдите\" в поликлинику.\n", fontSize = small)
        }
        "Выбрать специальность" -> {
            UsrItemsView(activity, user, model)

            val lpu = Lpu("0")
            lpu.name = user.Lpu
            lpu.lid = user.iLpu.toString()
            lpu.did = user.iDistr.toString()
            LpuItems(lpu = lpu, model = model)
        }
        "Выбрать врача" -> {
            UsrItemsView(activity, user, model)

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
            UsrItemsView(activity, user, model)

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
            UsrItemsView(activity, user, model)

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
    Text("Нажав на символ \"Карандаш\", отредактируйте персональные данные.", modifier = modBord)
    Spacer(Modifier.size(space))
    Text("Буквы в именах пишите так, как записано в регистратуре (\"ё\", \"-оглы\" и т.п.)", modifier = modFill)
    Spacer(Modifier.size(space))
    Text("Дату укажите в формате \"1984-09-23\" ", modifier = modBord)
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

