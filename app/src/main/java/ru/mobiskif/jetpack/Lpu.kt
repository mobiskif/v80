package ru.mobiskif.jetpack

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.room.*

@Entity(primaryKeys = ["did", "lid"])
data class Lpu(
    val did: String
    ,val lid: String
    ,val name: String?
    ,val description: String?
    ,val fullname: String?
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
            val element = Lpu(did, it["IdLPU"]!!, it["LPUShortName"], it["Description"],it["LPUFullName"])
            result=result.plusElement(element)
        }
    }
    return result
}

@Composable
fun LpuItems(lpu: Lpu, model: MainViewModel) {

    Row(modBord, horizontalArrangement = Arrangement.SpaceBetween) {
        Column(mod09.clickable {
            model.cuser.value?.iL=lpu.lid
            model.cuser.value?.Lpu=lpu.name
            //model.setCurrentUserLpu(lpu.lid, lpu.name)
            model.checkPatient(model.cuser.value!!)
            model.readSpecs(lpu.lid)
            model.setState("Выбрать специальность")
        }) {
            Text("${lpu.name}\n")
            Text("${lpu.fullname}", fontSize = small)
        }
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


