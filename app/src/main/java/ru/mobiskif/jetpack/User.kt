package ru.mobiskif.jetpack

import android.annotation.SuppressLint
import android.os.Environment
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.room.*

//@SuppressLint("NewApi")
@Entity
data class User(
    @PrimaryKey val id: Int,
    var F: String? = "",
    var I: String? = "",
    var O: String? = "",
    var D: String? = "",
    var iR: String? = "1",
    var iL: String? = "",
    var idPat: String? = "",
    var Distr: String? = "",
    var Lpu: String? = "",
    var Spec: String? = "",
    var Doc: String? = "",
    var Err: String? = "",
    var idAppointment: String? = "",
    var Photo: String? = Environment.getExternalStorageDirectory().path //"/storage/emulated/0"
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
fun UsrPhoto(user: User, model: Model) {
    Column(Modifier.clickable {
            model.setCurrentUser(user)
            model.setState("Выбрать фото")
        },
    ) {
        Image(
            painterResource(R.drawable.round_face),
            contentDescription = "Выбрать фото",
            //contentScale = ContentScale.Crop,
            //modifier = Modifier.border(1.dp, Color.Gray)
        )
    }
}


@Composable
fun UsrItems(user: User, model: Model) {
    Row(modFill) {
        UsrPhoto(user, model)
        Spacer(Modifier.width(space))
        Column(Modifier.clickable {
            if (model.getState() != "Выбрать клинику") {
                user.idPat = ""
                model.setCurrentUser(user)
                model.readLpus(user.iR.toString())
                model.setState("Выбрать клинику")
            }
        }) {
            Text("${user.F} \n${user.I} ${user.O}", fontWeight = FontWeight.Bold)
            Text("\n${user.D} \n${user.Distr} район")
        }
    }
    Spacer(Modifier.height(space))
}

@Composable
fun UsrItemsEdit(user: User, model: Model) {
    val fF = remember { mutableStateOf(TextFieldValue("${user.F}")) }
    val iI = remember { mutableStateOf(TextFieldValue("${user.I}")) }
    val oO = remember { mutableStateOf(TextFieldValue("${user.O}")) }
    val dD = remember { mutableStateOf(TextFieldValue("${user.D}")) }
    val rR = remember { mutableStateOf(TextFieldValue("${user.Distr}")) }
    val irR = remember { mutableStateOf(TextFieldValue("${user.iR}")) }
    Column(modFill) {
        //Row {
        UsrPhoto(user, model)
        Column {

            DistrictSpinner(model, rR, irR)

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

        }
        //}
        Spacer(modifier = Modifier.height(18.dp))
        Row {
            val fieldstouser: (User) -> Unit = {
                it.F = fF.value.text
                it.I = iI.value.text
                it.O = oO.value.text
                it.D = dD.value.text
                it.Distr = rR.value.text
                it.iR = irR.value.text
            }
            TextButton(onClick = {
                model.deleteUser(user)
                model.setState("Выбрать пациента")
            }) { Text("Удалить") }

            Button(onClick = {
                fieldstouser(user)
                model.updateUser(user)
                model.readLpus(user.iR.toString())
                model.setState("Выбрать клинику")
            }) { Text("Сохранить") }
        }

    }
}


