package ru.mobiskif.jetpack

import android.graphics.Paint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Alignment.Companion.Start
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

@Composable
fun mydialog(model: Model) {
    val lpu = model.clpu
    val openDialog = remember { mutableStateOf(true) }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onCloseRequest.
                openDialog.value = false
                model.setState("Выбрать клинику")
            },
            title = {
                Text(text = "${lpu.name}")
            },
            text = {
                Column {
                    if (!lpu.address.isNullOrEmpty()) Text("Адрес: ${lpu.address}\n")
                    if (!lpu.phone.isNullOrEmpty()) Text("Телефон: ${lpu.phone}\n")
                    if (!lpu.email.isNullOrEmpty()) Text("Почта: ${lpu.email}")
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        model.setState("Выбрать клинику")
                    }
                ) {
                    Text("Назад")
                }
            },
            /*dismissButton = {
                TextButton(
                    onClick = {
                        //openDialog.value = false
                        //model.setState("Выбрать клинику")
                    }
                ) {
                    Text("Dismiss")
                }
            }*/
        )
    }
}

@ExperimentalMaterialApi
@Composable
fun LpuItems(lpu: Lpu, model: Model) {
    val user = model.cuser.value!!
    val mod = if (model.getState() == "Выбрать клинику") modBord else modFill

    if (model.getState() == "Выбрать клинику")
    ListItem(
        icon = {
            Icon(
                Icons.Outlined.Info, "",
                Modifier
                    .clickable {
                        model.clpu = lpu
                        model.setState("Поликлиника")
                    }
                    .alpha(.33f)
                //.align(End)
            )
               },
        //overlineText = { Text("${lpu.lid}") },
        text = { Text("${lpu.name}") },
        secondaryText = { if (model.getState() == "Выбрать клинику") Text("${lpu.fullname} ${lpu.description}\n") },
        trailing = {
            if (model.getState() == "Выбрать клинику") {
                Icon(
                    Icons.Outlined.Delete, "",
                    Modifier
                        .clickable { model.deleteLpu(lpu) }
                        .alpha(.33f)
                    //.align(Start)
                )
            }
        },
        modifier = mb.clickable {
            user.iLpu = lpu.lid
            user.Lpu = lpu.name
            model.checkPatient(user)
            model.readSpecs(lpu.lid)
            model.setState("Выбрать специальность")
        }
    )
    else {
        ListItem(
            text = { Text("${lpu.name}") },
            secondaryText = { Text("Карточка: ${user.idPat}\n") },
            modifier = mf
        )
    }
/*
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
            Column(verticalArrangement = Arrangement.SpaceBetween, modifier = mb.fillMaxHeight()) { //Modifier.align(Alignment.Top)
                //Row(mb) {
                    Icon(
                        Icons.Outlined.Delete, "",
                        Modifier
                            .clickable { model.deleteLpu(lpu) }
                            .alpha(.33f)
                            //.align(Start)
                    )
                    //if (!lpu.phone.isNullOrEmpty()) Text("\n${lpu.phone}", textAlign = TextAlign.End, fontSize = small)
                    //if (!lpu.address.isNullOrEmpty()) Text("\n${lpu.address}", textAlign = TextAlign.End, fontSize = small)
                //}
                //Row(mb) {
                    Icon(
                        Icons.Outlined.Info, "",
                        Modifier
                            .clickable { model.setState("Инструкция") }
                            .alpha(.33f)
                            //.align(End)
                    )
                //}
            }
    }
 */
    Spacer(Modifier.height(space))

}

@Composable
fun LpuItemsOld(lpu: Lpu, model: Model) {
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
            Column(verticalArrangement = Arrangement.SpaceBetween, modifier = mb.fillMaxHeight()) { //Modifier.align(Alignment.Top)
                //Row(mb) {
                Icon(
                    Icons.Outlined.Delete, "",
                    Modifier
                        .clickable { model.deleteLpu(lpu) }
                        .alpha(.33f)
                    //.align(Start)
                )
                //if (!lpu.phone.isNullOrEmpty()) Text("\n${lpu.phone}", textAlign = TextAlign.End, fontSize = small)
                //if (!lpu.address.isNullOrEmpty()) Text("\n${lpu.address}", textAlign = TextAlign.End, fontSize = small)
                //}
                //Row(mb) {
                Icon(
                    Icons.Outlined.Info, "",
                    Modifier
                        .clickable { model.setState("Инструкция") }
                        .alpha(.33f)
                    //.align(End)
                )
                //}
            }
    }
    Spacer(Modifier.height(space))
}

