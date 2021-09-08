package ru.mobiskif.jetpack

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.room.*

@Entity
data class Spec(
    @PrimaryKey val id: String
    ,val name: String?
    ,val free: String?
)

fun fromSpecMap(map: MutableList<Map<String, String>>): List<Spec> {
    var result = listOf<Spec>()
    map.forEach {
        if (!it["IdSpesiality"].isNullOrEmpty()) {
            val element = Spec(it["IdSpesiality"]!!, it["NameSpesiality"], it["CountFreeParticipantIE"])
            result=result.plusElement(element)
        }
        else if (!it["ErrorDescription"].isNullOrEmpty()) {
            val element = Spec(it["IdError"]!!, it["ErrorDescription"], "")
            result=result.plusElement(element)
        }
        else if (it["Success"] == "false") {
            val element = Spec("0", "Учреждение вернуло пустой список", "")
            result=result.plusElement(element)
        }
    }
    return result
}

@Composable
fun SpecItems(spec: Spec, model: Model) {
    Row(modBord, horizontalArrangement = Arrangement.SpaceBetween) {
        Column(Modifier.clickable {
            model.cuser.value?.Spec=spec.name
            model.readDoctors(model.cuser.value?.iL.toString(), spec.id, model.cuser.value?.idPat.toString())
            model.setState("Выбрать врача")}
        ) {
            Text("${spec.name}",fontWeight = FontWeight.Bold)
            Text("${spec.free} талонов")
        }
    }
    Spacer(Modifier.height(space))
}
