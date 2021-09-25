package ru.mobiskif.jetpack

import android.util.Log
import androidx.room.*

@Entity(primaryKeys = ["did", "lid"])
data class Lpuf(
    var did: String = "",
    var lid: String = "",
    var name: String? = "",
    var description: String? = "",
    val fullname: String? = "",
    var address: String? = "",
    var phone: String? = "",
    var email: String? = ""
)

@Dao
interface LpufDao {
    @Insert
    fun createf(vararg lpus: Lpuf)

    @Query("SELECT * FROM lpuf")
    fun readf(): List<Lpuf>

    @Query("SELECT * FROM lpuf WHERE lid = :lid")
    fun readByIdf(lid: String): Lpuf

    @Update
    fun updatef(lpu: Lpuf)

    @Delete
    fun deletef(lpu: Lpuf)

    @Query("SELECT * FROM lpuf WHERE did = :did")
    fun readByDidf(did: String): List<Lpuf>
}

fun fromLpuMapF(map: MutableList<Map<String, String>>): List<Lpuf> {
    var result = listOf<Lpuf>()
    map.forEach {
        if (!it["ExternalHubId"].isNullOrEmpty()) {
            Log.d("jop","$it")
            val element = Lpuf(it["IdDistrict"]!!, it["ExternalHubId"]!!, it["LpuName"], "", it["LpuName"], it["Address"], it["PhoneCallCentre"], it["email"])
            result = result.plusElement(element)
        }
    }
    return result
}

