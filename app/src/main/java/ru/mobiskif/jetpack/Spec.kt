package ru.mobiskif.jetpack

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.room.*

@Entity
data class Spec(
    @PrimaryKey var id: String, var name: String? = "", var free: String? = "", var lpu: String? = ""
)

fun fromSpecMap(map: MutableList<Map<String, String>>): List<Spec> {
    var result = listOf<Spec>()
    map.forEach {
        if (!it["IdSpesiality"].isNullOrEmpty()) {
            val element = Spec(it["IdSpesiality"]!!, it["NameSpesiality"], it["CountFreeParticipantIE"], "")
            result = result.plusElement(element)
        } else if (!it["ErrorDescription"].isNullOrEmpty()) {
            val element = Spec(it["IdError"]!!, it["ErrorDescription"], "")
            result = result.plusElement(element)
        } else if (it["Success"] == "false") {
            val element = Spec("0", "Учреждение вернуло пустой список", "")
            result = result.plusElement(element)
        }
    }
    return result
}

@Composable
fun SpecItems(spec: Spec, model: Model) {
    val user = model.cuser.value!!
    val mod = if (model.getState() == "Выбрать специальность") modBord else modFill
    Row(mod, horizontalArrangement = Arrangement.SpaceBetween) {
        Column(Modifier.clickable {
            user.Spec = spec.name
            user.FreeSpec = spec.free
            user.iSpec = spec.id
            model.readDoctors(user.iLpu.toString(), spec.id, user.idPat.toString())
            model.setState("Выбрать врача")
        }
        ) {
            Text("${spec.name}")
            if (model.getState() == "Выбрать специальность") Text("Талонов ${spec.free}")
        }
    }
    Spacer(Modifier.height(space))
}
