package ru.mobiskif.jetpack

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Talon(
    @PrimaryKey var id: String,
    var date: String? = "",
    val free: String? = ""
)

fun fromTalonMap(map: MutableList<Map<String, String>>): List<Talon> {
    var result = listOf<Talon>()
    map.forEach {
        if (!it["IdAppointment"].isNullOrEmpty()) {
            val element = Talon(it["IdAppointment"]!!, it["VisitStart"], "")
            result = result.plusElement(element)
        } else if (!it["ErrorList"].isNullOrEmpty()) {
            val element = Talon(it["IdError"]!!, it["ErrorDescription"], "не дают")
            result = result.plusElement(element)
        } else if (it["Success"] == "false") {
            val element = Talon("0", "Учреждение вернуло пустой список", "не дают")
            result = result.plusElement(element)
        }
    }
    return result
}

@Composable
fun TalonItems(talon: Talon, model: Model) {
    val ar = talon.date.toString().split("T")
    var dat = ""
    var tim = ""
    if (ar.size > 1) {
        dat = ar[0]
        tim = ar[1].subSequence(0, 5).toString()
    }

    val user = model.cuser.value!!
    val mod = if (model.getState() == "Выбрать талон") modBord else modFill
    Row(mod.clickable {
        user.idAppointment = talon.id
        user.Dat = talon.date
        model.setState("Взять талон")
    })
    {
        Column (Modifier.fillMaxWidth(0.6f)) { Text("${user.Spec}") }
        Spacer(Modifier.size(space))
        Column {
            Text(tim, fontWeight = FontWeight.Bold)
            Text(dat)
            if (model.getState() == "Взять талон") {
                Spacer(Modifier.size(space))
                Button(onClick = {
                    model.getTalon(user.iLpu.toString(), user.idAppointment.toString(), user.idPat.toString())
                    model.setState("Выбрать клинику")
                }) { Text("Взять") }
            }
            else if (model.getState() == "Отменить талон") {
                Spacer(Modifier.size(space))
                Button(onClick = {
                    model.delTalon(user.iLpu.toString(), user.idAppointment.toString(), user.idPat.toString())
                    model.setState("Выбрать клинику")
                }) { Text("Отменить") }
            }
        }
    }
    Spacer(Modifier.height(space))
}

@Composable
fun TalonTake(model: Model) {
    val user = model.cuser.value!!
    val talon = Talon("0")
    talon.date = user.Dat
    talon.id = user.idAppointment.toString()
    TalonItems(talon = talon, model = model)

    TextButton(onClick = { model.setState("Выбрать клинику") }) { Text("Нет") }
}
