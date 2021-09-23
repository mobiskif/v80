package ru.mobiskif.jetpack

import android.app.Activity
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.room.*

//@SuppressLint("NewApi")
@Entity
data class User(
    @PrimaryKey var id: Int,
    var F: String? = "",
    var I: String? = "",
    var O: String? = "",
    var D: String? = "",
    var idPat: String? = "",
    var Distr: String? = "",
    var iDistr: String? = "1",
    var Lpu: String? = "",
    var iLpu: String? = "174",
    var Spec: String? = "",
    var iSpec: String? = "",
    var Doc: String? = "",
    var iDoc: String? = "",
    var Dat: String? = "",
    var FreeSpec: String? = "",
    var FreeDoc: String? = "",
    var idAppointment: String? = "",
    var Palette: String? = "Фиолетовая",
    var Photo: String? = "/storage/emulated/0"
)

@Dao
interface UserDao {
    @Insert
    fun create(vararg users: User)

    @Query("SELECT * FROM user")
    fun read(): List<User>

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)

    @Query("SELECT * FROM user WHERE id = :userId")
    fun readById(userId: Int): User

    @Query("SELECT * FROM user WHERE F LIKE :first AND I LIKE :last LIMIT 1")
    fun readByName(first: String, last: String): User

    @Query("SELECT max(id) FROM user")
    fun readMaxId(): Int

}

@Composable
fun UsrImage(bitmap: Bitmap) {
    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(space * 6)
            .clip(RoundedCornerShape(space * 3))
    )
}

@Composable
fun UsrPhotoView(activity: Activity, user: User, model: Model) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        UsrImage(loadFromInternalFolder(activity, "${user.id}.png"))
        TextButton(onClick = {
            model.setCurrentUser(user)
            model.setState("Изменить пациента")
        })
        { Text(text = "Изменить", fontSize = small) }
    }
}

@Composable
fun UsrPhotoEdit(activity: Activity, user: User, model: Model) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        UsrImage(loadFromInternalFolder(activity, "${user.id}.png"))
/*
        TextButton(onClick = {
            //model.setCurrentUser(user)
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE) //intent.putExtra("fname", "${user.id}.png")
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                if (checkPermissionForCamera(activity)) {
                    startActivityForResult(activity, intent, 1, null)
                } else {
                    requestPermissionForCamera(activity)
                    //startActivityForResult(activity, intent, 1, null)
                }
            } else startActivityForResult(activity, intent, 1, null)
        })
        { Text(text = "Сделать фото", fontSize = small) }

        TextButton(onClick = {
            val st = model.getState()
            //model.setState("Инструкция")
            deleteFromInternalFolder(activity, "${user.id}.png")
            //model.setState(st)
            model.repaint()
        })
        { Text(text = "Сбросить", fontSize = small) }
*/
    }
}

@ExperimentalMaterialApi
@Composable
fun ShortUserList(activity: Activity, user: User, model: Model) {
    ListItem(
        overlineText = { Text("${user.Distr} район") },
        text = { Text("${user.F} ${user.I} ${user.O}") },
        modifier = Modifier.background(MaterialTheme.colors.secondary, RoundedCornerShape(space)).absolutePadding(0.dp,0.dp,0.dp,space)
    )
}

@ExperimentalMaterialApi
@Composable
fun WideUserList(activity: Activity, user: User, model: Model) {
    if (user.Distr.isNullOrEmpty()) {
        ListItem(
            icon = { UsrImage(loadFromInternalFolder(activity, "${user.id}.png")) },
            text = { Text("Нажмите и заполните все данные пациента") },
            modifier = Modifier.background(MaterialTheme.colors.secondary, RoundedCornerShape(space)).clickable {
                model.setCurrentUser(user)
                model.setState("Изменить пациента")
            }
        )
    }
    else {
        ListItem(
            icon = { UsrImage(loadFromInternalFolder(activity, "${user.id}.png")) },
            overlineText = { Text("${user.Distr} район") },
            text = { Text("${user.F} ${user.I} ${user.O}") },
            secondaryText = { Text("${user.D}\n") },
            trailing = {
                Icon(Icons.Filled.Edit, "",
                    Modifier.alpha(.33f).clickable {
                        model.setCurrentUser(user)
                        model.setState("Изменить пациента")
                    })
            },
            modifier = Modifier.background(MaterialTheme.colors.secondary, RoundedCornerShape(space))
        )
    }
}

@ExperimentalMaterialApi
@Composable
fun UsrItemsView(activity: Activity, user: User, model: Model) {
    Column(modifier = Modifier.clickable {
        user.idPat = ""
        model.setCurrentUser(user)
        model.readLpus(user.iDistr.toString(), user.id.toString())
        model.setState("Выбрать клинику")
    }) {
        when (model.getState()) {
            "Выбрать пациента" -> WideUserList(activity, user, model)
            else -> ShortUserList(activity, user, model)
        }
    }
    Spacer(Modifier.size(space))
}

@Composable
fun UsrDataEdit(activity: Activity, user: User, model: Model) {
    val fF = remember { mutableStateOf(TextFieldValue("${user.F}")) }
    val iI = remember { mutableStateOf(TextFieldValue("${user.I}")) }
    val oO = remember { mutableStateOf(TextFieldValue("${user.O}")) }
    val dD = remember { mutableStateOf(TextFieldValue("${user.D}")) }
    val rR = remember { mutableStateOf(TextFieldValue("${user.Distr}")) }
    val irR = remember { mutableStateOf(TextFieldValue("${user.iDistr}")) }
    Row {
        Column {
            UsrPhotoEdit(activity, user, model)
        }
        Spacer(Modifier.size(space))
        Column {
            //Row {
            Column {
                OutlinedTextField(
                    value = fF.value,
                    onValueChange = { fF.value = it },
                    label = { Text("Фамилия") }
                )
                OutlinedTextField(
                    value = iI.value,
                    onValueChange = { iI.value = it },
                    label = { Text("Имя") }
                )
                OutlinedTextField(
                    value = oO.value,
                    onValueChange = { oO.value = it },
                    label = { Text("Отчество") }
                )
                OutlinedTextField(
                    value = dD.value,
                    onValueChange = { dD.value = it },
                    label = { Text("Дата рождения") },
                    placeholder = { Text(text = "1986-04-26") }
                )
                //Spacer(Modifier.height(space))
                DistrictSpinner(model, rR, irR)
            }
            //}
            Spacer(modifier = Modifier.height(space * 2))
            Row {
                val fieldstouser: (User) -> Unit = {
                    it.F = fF.value.text
                    it.I = iI.value.text
                    it.O = oO.value.text
                    it.D = dD.value.text
                    it.Distr = rR.value.text
                    it.iDistr = irR.value.text
                }
                TextButton(onClick = {
                    model.deleteUser(user)
                    model.setState("Выбрать пациента")
                }) { Text("Удалить") }

                Button(onClick = {
                    fieldstouser(user)
                    model.updateUser(user)
                    model.readLpus(user.iDistr.toString(), user.id.toString())
                    model.setState("Выбрать клинику")
                }) { Text("Записать") }
            }
        }
    }
}


