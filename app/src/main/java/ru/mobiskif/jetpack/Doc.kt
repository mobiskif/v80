package ru.mobiskif.jetpack

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.room.*

@Entity
data class Doc(
    @PrimaryKey val id: String
    ,val name: String?
    ,val free: String?
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
fun DocItems(doc: Doc, model: MainViewModel) {
    Row(modBord, horizontalArrangement = Arrangement.SpaceBetween) {
        Column(Modifier.clickable {
            model.cuser.value?.Doc=doc.name
            model.readTalons(model.cuser.value?.iL.toString(), doc.id, model.cuser.value?.idPat.toString())
            model.setState("Выбрать талон")}
        ) {
            Text("${doc.name}",fontWeight = FontWeight.Bold)
            Text("талонов ${doc.free}")
        }
    }
    Spacer(Modifier.height(space))
}

