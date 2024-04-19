package com.jose.magiccraftapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.jose.magiccraftapp.data.entity.Mazo

@Dao
interface MazoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(mazo: Mazo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(mazos: MutableList<Mazo>)

    @Update
    fun update(vararg mazo: Mazo)

    @Delete
    fun delete(vararg mazo: Mazo)

}