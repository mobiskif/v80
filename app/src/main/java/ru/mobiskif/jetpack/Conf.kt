package ru.mobiskif.jetpack

import androidx.room.*

@Entity(primaryKeys = ["name"])
data class Conf(
    var name: String = "palette",
    var value: String = "Фиолетовая",
)

@Dao
interface ConfDao {
    @Insert
    fun create(vararg confs: Conf)

    @Query("SELECT * FROM conf")
    fun read(): List<Conf>

    @Query("SELECT * FROM conf WHERE name = :name")
    fun readByName(name: String): Conf

    @Update
    fun update(conf: Conf)

    @Delete
    fun delete(conf: Conf)

}


