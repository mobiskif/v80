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
    @PrimaryKey val id: String, val name: String?, val free: String?
)

fun fromTalonMap(map: MutableList<Map<String, String>>): List<Talon> {
    var result = listOf<Talon>()
    map.forEach {
        if (!it["IdAppointment"].isNullOrEmpty()) {
            //val d = it["VisitStart"]!!.split("T")[0]
            //val t = it["VisitStart"]!!.split("T")[1].split(":")[0] +":" + it["VisitStart"]!!.split("T")[1].split(":")[1]
            //val element = Talon(t, d, "")
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
    val ar = talon.name.toString().split("T")
    var dat = ""
    var tim = ""
    if (ar.size>1) {
        dat = ar[0]
        tim = ar[1].subSequence(0, 5).toString()
    }
    Row(modBord.clickable {
        model.cuser.value?.idAppointment = talon.id
        model.cuser.value?.Err = talon.name
        model.setState("Взять талон")
    }
    ) {
        Column(Modifier.fillMaxWidth(.3f)) {
            Text("Талон:")
        }
        Column {
            Text(tim, fontWeight = FontWeight.Bold)
            Text(dat)
            //Spacer(Modifier.size(space))
            //Button(onClick = {  }) { Text("Взять")  }
        }
    }
    Spacer(Modifier.height(space))
}

@Composable
fun TalonTake(model: Model) {
    val user = model.cuser.value!!
    val idPat = model.cuser.value?.idPat.toString()
    val idLpu = model.cuser.value?.iL.toString()
    val idAppointment = model.cuser.value?.idAppointment.toString()
    val ar = model.cuser.value?.Err.toString().split("T")
    var dat = ""
    var tim = ""
    if (ar.size>1) {
        dat = model.cuser.value?.Err.toString().split("T")[0]
        tim = model.cuser.value?.Err.toString().split("T")[1].subSequence(0, 5).toString()
    }
    Row(modBord) {
        Column(Modifier.fillMaxWidth(.3f)) {
            Text("Пациент:")
        }
        Column {
            Text("${user.F} \n${user.I} ${user.O}")
        }
    }
    Spacer(Modifier.size(space))
    Row(modBord) {
        Column(Modifier.fillMaxWidth(.3f)) {
            Text("Врач:")
        }
        Column {
            Text("${user.Lpu} \n${user.Spec}")
            Text("${user.Doc}")
        }
    }
    Spacer(Modifier.size(space))
    Row(modFill) {
        Column(Modifier.fillMaxWidth(.3f)) {
            Text("Талон:")
        }
        Column {
            Text(tim, fontWeight = FontWeight.Bold)
            Text(dat)
            Spacer(Modifier.size(space))
            Button(onClick = {
                model.getTalon(idLpu, idAppointment, idPat)
                model.setState("Выбрать клинику")
            }) { Text("Взять") }
        }
    }
    Spacer(Modifier.size(space))
    TextButton(onClick = { model.setState("Выбрать клинику") }) { Text("Нет") }
}


@Composable
fun TalonBrake(model: Model) {
    val user = model.cuser.value!!
    val idPat = model.cuser.value?.idPat.toString()
    val idLpu = model.cuser.value?.iL.toString()
    val idAppointment = model.cuser.value?.idAppointment.toString()
    val dat = model.cuser.value?.Err.toString().split("T")[0]
    val tim = model.cuser.value?.Err.toString().split("T")[1].subSequence(0, 5)
    Row(modBord) {
        Column(Modifier.fillMaxWidth(.3f)) {
            Text("Пациент:")
        }
        Column {
            Text("${user.F} \n${user.I} ${user.O}")
        }
    }
    Spacer(Modifier.size(space))
    Row(modBord) {
        Column(Modifier.fillMaxWidth(.3f)) {
            Text("Врач:")
        }
        Column {
            Text("${user.Lpu} \n${user.Spec}")
            Text("${user.Doc}")
        }
    }
    Spacer(Modifier.size(space))
    Row(modFill) {
        Column(Modifier.fillMaxWidth(.3f)) {
            Text("Талон:")
        }
        Column {
            Text("$tim", fontWeight = FontWeight.Bold)
            Text(dat)
            Spacer(Modifier.size(space))
            Button(onClick = {
                model.delTalon(idLpu, idAppointment, idPat)
                model.setState("Выбрать клинику")
            }) { Text("Отменить") }
        }
    }
    Spacer(Modifier.size(space))
    TextButton(onClick = { model.setState("Выбрать клинику") }) { Text("Нет") }
}
