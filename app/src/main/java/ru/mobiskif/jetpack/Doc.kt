package ru.mobiskif.jetpack

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.room.*

@Entity
data class Doc(
    @PrimaryKey var id: String
    , var name: String?=""
    , var free: String?=""
)

fun fromDocMap(map: MutableList<Map<String, String>>): List<Doc> {
    var result = listOf<Doc>()
    map.forEach {
        if (!it["Name"].isNullOrEmpty()) {
            val element = Doc(it["IdDoc"]!!, it["Name"], it["CountFreeParticipantIE"])
            result=result.plusElement(element)
        }
        else if (!it["ErrorDescription"].isNullOrEmpty()) {
            val element = Doc(it["IdError"]!!, it["ErrorDescription"], "не дают")
            result=result.plusElement(element)
        }
        else if (it["Success"] == "false") {
            val element = Doc("0", "Учреждение вернуло пустой список", "не дают")
            result=result.plusElement(element)
        }
    }
    return result
}

@Composable
fun DocItems(doc: Doc, model: Model) {
    val user = model.cuser.value!!
    val mod = if (model.getState() == "Выбрать врача") mbp else mfp
    Row(mod, horizontalArrangement = Arrangement.SpaceBetween) {
        Column(Modifier.clickable {
            user.Doc=doc.name
            user.FreeDoc=doc.free
            user.iDoc=doc.id
            model.readTalons(user.iLpu.toString(), doc.id, user.idPat.toString())
            model.setState("Выбрать талон")}
        ) {
            Text("${doc.name}")
            if (model.getState()=="Выбрать врача") Text("Талонов ${doc.free}")
        }
    }
    Spacer(Modifier.height(space))
}

