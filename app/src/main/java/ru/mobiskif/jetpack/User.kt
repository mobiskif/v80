package ru.mobiskif.jetpack

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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.room.*

@Entity
data class User(
    @PrimaryKey val id: Int
    , var F: String?=""
    , var I: String?=""
    , var O: String?=""
    , var D: String?=""
    , var iR: String?="1"
    , var iL: String?=""
    , var idPat: String?=""
    , var Distr: String?=""
    , var Lpu: String?=""
    , var Spec: String?=""
    , var Doc: String?=""
    , var Err: String?=""
    , var idAppointment: String?=""
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
fun UsrItems(user: User, model: MainViewModel) {
    Row(modFill,horizontalArrangement = Arrangement.SpaceBetween) {
        Column(mod09.clickable {
            model.setCurrentUser(user)
            model.readLpus(user.iR.toString())
            model.setState("Выбрать клинику")}
        ) {
            Text("${user.F} \n${user.I} ${user.O}",fontWeight = FontWeight.Bold)
            Text("\n${user.D} \n${user.Distr} район")
        }
        Column (Modifier.align(Alignment.Bottom)) {
            Icon(
                Icons.Filled.Edit, "Edit",
                Modifier
                    .alpha(.33f)
                    .clickable {
                        model.setCurrentUser(user)
                        model.setState("Изменить пациента")
                    })
        }
    }
    Spacer(Modifier.height(space))
}

@Composable
fun UsrItemsEdit(user: User, model: MainViewModel) {
    val fF = remember { mutableStateOf(TextFieldValue("${user.F}")) }
    val iI = remember { mutableStateOf(TextFieldValue("${user.I}")) }
    val oO = remember { mutableStateOf(TextFieldValue("${user.O}")) }
    val dD = remember { mutableStateOf(TextFieldValue("${user.D}")) }
    val rR = remember { mutableStateOf(TextFieldValue("${user.Distr}")) }
    val irR = remember { mutableStateOf(TextFieldValue("${user.iR}")) }
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

        val fieldstouser: (User) -> Unit = {
            it.F = fF.value.text
            it.I = iI.value.text
            it.O = oO.value.text
            it.D = dD.value.text
            it.Distr = rR.value.text
            it.iR = irR.value.text
        }
        Spacer(modifier = Modifier.height(18.dp))
        Row {
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


