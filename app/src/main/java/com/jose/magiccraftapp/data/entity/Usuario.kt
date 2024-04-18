package com.jose.magiccraftapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = Usuario.TABLE_NAME)
class Usuario(
    @PrimaryKey(autoGenerate = false) val idUsuario: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "surname") val surname: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "typeUser") val typeUser: String
) {
    companion object{
        const val TABLE_NAME = "usuario"
    }
}