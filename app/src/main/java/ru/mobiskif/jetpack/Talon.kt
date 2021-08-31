package ru.mobiskif.jetpack

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.room.*

@Entity
data class Talon(
    @PrimaryKey val id: String
    ,val name: String?
    ,val free: String?
)

fun fromTalonMap(map: MutableList<Map<String, String>>): List<Talon> {
    var result = listOf<Talon>()
    map.forEach {
        if (!it["IdAppointment"].isNullOrEmpty()) {
            //val d = it["VisitStart"]!!.split("T")[0]
            //val t = it["VisitStart"]!!.split("T")[1].split(":")[0] +":" + it["VisitStart"]!!.split("T")[1].split(":")[1]
            //val element = Talon(t, d, "")
            val element = Talon(it["IdAppointment"]!!, it["VisitStart"], "")
            result=result.plusElement(element)
        }
        else if (!it["ErrorList"].isNullOrEmpty()) {
            val element = Talon(it["IdError"]!!, it["ErrorDescription"], "не дают")
            result=result.plusElement(element)
        }
        else if (it["Success"] == "false") {
            val element = Talon("0", "Учреждение вернуло пустой список", "не дают")
            result=result.plusElement(element)
        }
    }
    return result
}

@Composable
fun TalonItems(talon: Talon, model: MainViewModel) {
    Row(modBord, horizontalArrangement = Arrangement.SpaceBetween) {
        Column(Modifier.clickable {
            model.cuser.value?.idAppointment = talon.id
            model.setState("Взять талон")}
        ) {
            Text("${talon.id}",fontWeight = FontWeight.Bold)
            Text("${talon.name}")
            Text("${talon.free}")
        }
    }
    Spacer(Modifier.height(space))
}

@Composable
fun TalonTake(model: MainViewModel) {
    val idPat = model.cuser.value?.idPat.toString()
    val idLpu = model.cuser.value?.iL.toString()
    val idAppointment = model.cuser.value?.idAppointment.toString()
    Row(modBord, horizontalArrangement = Arrangement.SpaceBetween) {
        Column(Modifier.clickable {
            model.getTalon(idLpu, idAppointment, idPat)}
        ) {
            Text("$idAppointment",fontWeight = FontWeight.Bold)
            Text("$idLpu")
            Text("$idPat")
        }
    }
    Spacer(Modifier.height(space))
}

@Composable
fun TalonBrake(model: MainViewModel) {
    val idPat = model.cuser.value?.idPat.toString()
    val idLpu = model.cuser.value?.iL.toString()
    val idAppointment = model.cuser.value?.idAppointment.toString()
    Row(modBord, horizontalArrangement = Arrangement.SpaceBetween) {
        Column(Modifier.clickable {
            model.delTalon(idLpu, idAppointment, idPat)}
        ) {
            Text("$idAppointment",fontWeight = FontWeight.Bold)
            Text("$idLpu")
            Text("$idPat")
        }
    }
    Spacer(Modifier.height(space))
}
