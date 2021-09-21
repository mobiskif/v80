package ru.mobiskif.jetpack

import android.graphics.Paint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.room.*

@Entity(primaryKeys = ["did", "uid", "lid"])
data class Lpu(
    var did: String = "",
    var uid: String = "",
    var lid: String = "",
    var name: String? = "",
    var description: String? = "",
    val fullname: String? = "",
    var address: String? = "",
    var phone: String? = "",
    var email: String? = ""
)

@Dao
interface LpuDao {
    @Insert
    fun create(vararg lpus: Lpu)

    @Query("SELECT * FROM lpu")
    fun read(): List<Lpu>

    @Query("SELECT * FROM lpu WHERE uid = :uid AND lid = :lid")
    fun readById(uid: String, lid: String): Lpu

    @Update
    fun update(lpu: Lpu)

    @Delete
    fun delete(lpu: Lpu)

    @Query("SELECT * FROM lpu WHERE did = :did AND uid = :uid")
    fun readByDid(did: String, uid: String): List<Lpu>
}

fun fromLpuMap(did: String, uid: String, map: MutableList<Map<String, String>>): List<Lpu> {
    var result = listOf<Lpu>()
    map.forEach {
        if (!it["IdLPU"].isNullOrEmpty()) {
            val element = Lpu(did, uid, it["IdLPU"]!!, it["LPUShortName"], it["Description"], it["LPUFullName"])
            result = result.plusElement(element)
        }
    }
    return result
}

@ExperimentalMaterialApi
@Composable
fun LpuItems2(lpu: Lpu, model: Model) {
    val user = model.cuser.value!!
    //BadgeBox(badgeContent = { Text("88") }) {

        Card(elevation = 3.dp, modifier = Modifier.clickable {
            user.iLpu = lpu.lid
            user.Lpu = lpu.name
            model.checkPatient(user)
            model.readSpecs(lpu.lid)
            model.setState("Выбрать специальность")
        }) {
            ListItem(
                //icon = { UsrImage(loadFromInternalFolder(activity, "${user.id}.png")) },
                secondaryText = { Text("${lpu.fullname}\n") },
                trailing = {
                    Icon(
                        Icons.Filled.Delete, "Удалить",
                        Modifier
                            .clickable { model.deleteLpu(lpu) }
                            .alpha(.33f)
                    )
                },
                //overlineText = { Text("${lpu.fullname}") },
                singleLineSecondaryText = false
            ) {
                Text("${lpu.name}")
            }
        }
    //}
    Spacer(Modifier.size(space))
}

@Composable
fun LpuItems(lpu: Lpu, model: Model) {
    val user = model.cuser.value!!
    val mod = if (model.getState() == "Выбрать клинику") modBord else modFill
    Row(mod, horizontalArrangement = Arrangement.SpaceBetween) {
        Column(mod09.clickable {
            user.iLpu = lpu.lid
            user.Lpu = lpu.name
            model.checkPatient(user)
            model.readSpecs(lpu.lid)
            model.setState("Выбрать специальность")
        })
        {
            Text("${lpu.name}")
            if (model.getState() == "Выбрать клинику") Text("\n${lpu.fullname} (${lpu.description})", fontSize = small)
            else Text("Карточка ${user.idPat}")
        }
        if (model.getState() == "Выбрать клинику")
            Column(Modifier.align(Alignment.Top)) {
                Icon(
                    Icons.Filled.Delete, "Удалить",
                    Modifier
                        .clickable { model.deleteLpu(lpu) }
                        .alpha(.33f).align(End)
                )
                if (!lpu.phone.isNullOrEmpty()) Text("\n${lpu.phone}", textAlign = TextAlign.End, fontSize = small)
            }
    }
    Spacer(Modifier.height(space))
}


