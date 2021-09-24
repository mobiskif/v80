package ru.mobiskif.jetpack

import android.util.Log
import androidx.room.*

@Entity
data class Distr(
    @PrimaryKey val id: String
    ,val name: String?
)

@Dao
interface DistrDao {
    @Insert
    fun create(vararg lpus: Distr)

    @Query("SELECT * FROM distr")
    fun read(): List<Distr>

    @Update
    fun update(lpu: Distr)

    @Delete
    fun delete(lpu: Distr)

    @Query("SELECT * FROM distr WHERE id = :id")
    fun readById(id: Int): Distr

}

fun fromDistrMap(map: MutableList<Map<String, String>>): List<Distr> {
    var result = listOf<Distr>()
    map.forEach {
        if (!it["IdDistrict"].isNullOrEmpty()) {
            val element = Distr(it["IdDistrict"]!!, it["DistrictName"])
            result=result.plusElement(element)
            Log.d("jop","$it")
        }
    }
    return result
}


