package com.jose.magiccraftapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.jose.magiccraftapp.data.entity.Carta

@Dao
interface CartaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(carta: Carta)

    @Update
    fun update(vararg carta: Carta)

    @Delete
    fun delete(vararg carta: Carta)

}