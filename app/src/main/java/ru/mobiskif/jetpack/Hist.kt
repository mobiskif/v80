package ru.mobiskif.jetpack

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.room.*

@Entity(primaryKeys = ["uid", "lid"])
data class Hist(
    val uid: String
    ,val lid: String
    ,val name: String?
    ,val date: String?
    ,val spec: String?
)

@Dao
interface HistDao {
    @Insert
    fun create(vararg hists: Hist)

    @Query("SELECT * FROM hist")
    fun read(): List<Hist>

    @Update
    fun update(lpu: Hist)

    @Delete
    fun delete(lpu: Hist)

    @Query("SELECT * FROM hist WHERE uid = :uid")
    fun readByUid(uid: String): List<Hist>
}

fun fromHistMap(idLpu:String, idPat:String, map: MutableList<Map<String, String>>): List<Hist> {
    var result = listOf<Hist>()
    map.forEach {
        if (!it["IdAppointment"].isNullOrEmpty()) {
            val element = Hist(idLpu, idPat, it["Name"], it["VisitStart"], it["NameSpesiality"])
            result=result.plusElement(element)
        }
        else if (!it["ErrorList"].isNullOrEmpty()) {
            val element = Hist(idLpu, idPat, it["ErrorDescription"],"VisitStart", "NameSpesiality")
            result=result.plusElement(element)
        }
        else if (it["Success"] == "false") {
            val element = Hist(idLpu, idPat, it["ErrorDescription"],"VisitStart", "IdSpesiality")
            result=result.plusElement(element)
        }
    }
    return result
}

@Composable
fun HistItems(hist: Hist, model: MainViewModel) {
    Row(modFillVar, horizontalArrangement = Arrangement.SpaceBetween) {
        Column(Modifier.clickable {
            model.setState("Отменить талон")}
        ) {
            Text("Запись на ${hist.date!!.split("T")[1].subSequence(0,5)}",fontWeight = FontWeight.ExtraBold)
            Text("${hist.date!!.split("T")[0]}")
            Text("${hist.name}", fontStyle = FontStyle.Italic)
            Text("${hist.spec}")
        }
    }
    Spacer(Modifier.size(space))
}

