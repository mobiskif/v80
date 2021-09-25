package ru.mobiskif.jetpack

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.room.*

@Entity(primaryKeys = ["uid", "lpu", "spec"])
data class Hist(
    val uid: String,
    val lpu: String,
    val spec: String,
    val name: String? = "",
    val date: String? = "",
    val idLpu: String? = "",
    val idPat: String? = "",
    val idAppointment: String? = ""
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
            val element = Hist(user.id.toString(), user.Lpu.toString(), it["NameSpesiality"]!!, it["Name"], it["VisitStart"], user.iLpu, user.idPat, it["IdAppointment"])
            result = result.plusElement(element)
        }
    }
    return result
}

@Composable
fun HistItems(hist: Hist, model: Model) {
    val ar = hist.date.toString().split("T")
    var dat = ""
    var tim = ""
    if (ar.size > 1) {
        dat = ar[0]
        tim = ar[1].subSequence(0, 5).toString()
    }

    val user = model.cuser.value!!
    val mod = if (model.getState() == "Выбрать талон") mbp else mfp
    Row(mod.clickable {
        user.iLpu = hist.idLpu
        user.idPat = hist.idPat
        user.Spec = hist.spec

        user.idAppointment = hist.idAppointment
        user.Dat = hist.date
        model.setState("Отменить талон")
    })
    {
        Column (Modifier.widthIn(56.dp, 156.dp)) { Text(hist.spec) }
        Spacer(Modifier.size(space))
        Column {
            Text(tim, fontWeight = FontWeight.Bold)
            Text(dat)
        }
    }
    Spacer(Modifier.size(space))
}

