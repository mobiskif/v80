package ru.mobiskif.jetpack

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.room.*

@Entity(primaryKeys = ["did", "lid"])
data class Lpu(
    var did: String = "", var lid: String = "", var name: String? = "", val description: String? = "", val fullname: String? = ""
)

@Dao
interface LpuDao {
    @Insert
    fun create(vararg lpus: Lpu)

    @Query("SELECT * FROM lpu")
    fun read(): List<Lpu>

    @Update
    fun update(lpu: Lpu)

    @Delete
    fun delete(lpu: Lpu)

    @Query("SELECT * FROM lpu WHERE did = :did")
    fun readByDid(did: String): List<Lpu>
}

fun fromLpuMap(did: String, map: MutableList<Map<String, String>>): List<Lpu> {
    var result = listOf<Lpu>()
    map.forEach {
        if (!it["IdLPU"].isNullOrEmpty()) {
            val element = Lpu(did, it["IdLPU"]!!, it["LPUShortName"], it["Description"], it["LPUFullName"])
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

        Card(elevation = space / 2, modifier = Modifier.clickable {
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
            if (model.getState() == "Выбрать клинику") Text("${lpu.fullname}", fontSize = small)
            else Text("Карточка ${user.idPat}")
        }
        if (model.getState() == "Выбрать клинику")
            Column(Modifier.align(Alignment.Bottom)) {
                Icon(
                    Icons.Filled.Delete, "Удалить",
                    Modifier
                        .clickable { model.deleteLpu(lpu) }
                        .alpha(.33f)
                )
            }
    }
    Spacer(Modifier.height(space))
}


