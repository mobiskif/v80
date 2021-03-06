package ru.mobiskif.jetpack

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
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
fun LpuInfoDialog(activity: MainActivity, model: Model) {
    val lpu = model.clpu
    val openDialog = remember { mutableStateOf(true) }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
                model.setState("?????????????? ??????????????")
            },
            title = { Text(text = "${lpu.fullname}") },
            text = {
                Column {
                    //Row(Modifier.fillMaxHeight(.4f)) {
                    Row {
                        Text("??????.: ", fontWeight = FontWeight.Bold)
                        Text("${lpu.phone}", modifier = Modifier.clickable {
                            val intent = Intent(Intent.ACTION_CALL)
                            intent.data = Uri.parse("tel:${lpu.phone}")
                            //activity.startActivity(intent)
                            //if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                                if (checkPermissionForCall(activity)) activity.startActivity(intent)
                                else requestPermissionForCall(activity)
                            //} else activity.startActivity(intent) //activity.callLauncher.launch(intent)

                        })
                    }
                    Row {
                        Text("??????????: ", fontWeight = FontWeight.Bold)
                        Text("${lpu.address}")
                    }
                    Row {
                        Text("??????????: ", fontWeight = FontWeight.Bold)
                        Text("${lpu.email}")
                    }
                    Spacer(Modifier.size(space))
                    //}
                    Row(Modifier.fillMaxHeight(.7f)) {
                        Mymap(lpu)
                    }
                }

                /*
                val intent = Intent(Intent.ACTION_CALL);
                intent.data = Uri.parse("tel:$number")
                startActivity(intent)
                 */
            },

            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        model.setState("?????????????? ??????????????")
                    }
                ) {
                    Text("??????????")
                }
            },

            )
    }
}

@ExperimentalMaterialApi
@Composable
fun LpuItems(lpu: Lpu, model: Model) {
    val user = model.cuser.value!!

    if (model.getState() == "?????????????? ??????????????")
        ListItem(
            icon = {
                Icon(
                    Icons.Outlined.LocationOn, "",
                    Modifier
                        .alpha(.33f)
                        .clickable {
                            model.clpu = lpu
                            model.setState("??????????????????????")
                        }
                )
            },
            text = { Text("${lpu.name}") },
            secondaryText = { if (model.getState() == "?????????????? ??????????????") Text("${lpu.address} ${lpu.description}\n") },
            trailing = {
                if (model.getState() == "?????????????? ??????????????") {
                    Icon(
                        Icons.Outlined.Delete, "",
                        Modifier
                            .alpha(.33f)
                            .clickable { model.deleteLpu(lpu) }
                    )
                }
            },
            modifier = mb.clickable {
                user.iLpu = lpu.lid
                user.Lpu = lpu.name
                model.checkPatient(user)
                model.readSpecs(lpu.lid)
                //model.readHists(user)
                model.setState("?????????????? ??????????????????????????")
            }
        )
    else {
        ListItem(
            text = { Text("${lpu.name}") },
            secondaryText = { Text("????????????????: ${user.idPat}\n") },
            modifier = mf
        )
    }
    Spacer(Modifier.height(space))
}


