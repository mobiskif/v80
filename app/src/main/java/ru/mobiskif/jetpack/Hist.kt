package ru.mobiskif.jetpack

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.room.*

@Entity(primaryKeys = ["uid", "lpu", "spec"])
data class Hist(
    val uid: String
    ,val lpu: String
    ,val spec: String
    ,val name: String?=""
    ,val date: String?=""
    ,val idLpu: String?=""
    ,val idPat: String?=""
    ,val idAppointment: String?=""
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

    @Query("SELECT * FROM hist WHERE uid = :uid AND lpu = :lpu")
    fun readByUidLid(uid: String, lpu: String): List<Hist>

    @Query("SELECT * FROM hist WHERE uid = :uid")
    fun readByUid(uid: String): List<Hist>

}

fun fromHistMap(user: User, map: MutableList<Map<String, String>>): List<Hist> {
    var result = listOf<Hist>()
    map.forEach {
        if (!it["IdAppointment"].isNullOrEmpty()) {
            val element = Hist(user.id.toString(), user.Lpu.toString(), it["NameSpesiality"]!!, it["Name"], it["VisitStart"],user.iL,user.idPat,it["IdAppointment"] )
            result=result.plusElement(element)
        }
        else if (!it["ErrorList"].isNullOrEmpty()) {
            //val element = Hist(uid, idLpu, it["ErrorDescription"],"VisitStart", "NameSpesiality")
            //result=result.plusElement(element)
        }
        else if (it["Success"] == "false") {
            //val element = Hist(uid, idLpu, it["ErrorDescription"],"VisitStart", "IdSpesiality")
            //result=result.plusElement(element)
        }
    }
    return result
}

@Composable
fun HistItems(hist: Hist, model: MainViewModel) {
    Row(modFillVar, horizontalArrangement = Arrangement.SpaceBetween) {
        Column(mod09.clickable {
            model.cuser.value?.idAppointment = hist.idAppointment
            model.cuser.value?.iL = hist.idLpu
            model.cuser.value?.idPat = hist.idPat
            model.cuser.value?.Err = hist.date
            model.cuser.value?.Spec = hist.spec
            model.setState("Отменить талон")}
        ) {
            Text("${hist.date!!.split("T")[0]}")
            Text("${hist.lpu}")
            Text("${hist.spec}")
        }
        Spacer(Modifier.size(space))
        Column(modBord){
            Text("${hist.date!!.split("T")[1].subSequence(0,5)}",fontWeight = FontWeight.ExtraBold)
        }
    }
    Spacer(Modifier.size(space))
}

