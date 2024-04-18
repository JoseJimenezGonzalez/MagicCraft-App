package com.jose.magiccraftapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = Mazo.TABLE_NAME)
class Mazo(
    @PrimaryKey(autoGenerate = false) val idMazo: String,
    @ColumnInfo(name = "idUsuario") val idUsuario: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "format") val format: String,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "urlImageFirebase") val urlImageFirebase: String
) {
    companion object{
        const val TABLE_NAME = "mazo"
    }
}