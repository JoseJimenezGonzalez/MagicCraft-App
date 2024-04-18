package com.jose.magiccraftapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.jose.magiccraftapp.data.entity.Usuario

@Dao
interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(usuario: Usuario)

    @Update
    fun update(vararg usuario: Usuario)

    @Delete
    fun delete(vararg usuario: Usuario)

}