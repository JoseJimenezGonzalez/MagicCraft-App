package com.jose.magiccraftapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = Carta.TABLE_NAME)
class Carta(
    @PrimaryKey(autoGenerate = false) val idCarta: String,
    @ColumnInfo(name = "idMazo") val idMazo: String,
    @ColumnInfo(name = "cmc") val cmc: Int,
    @ColumnInfo(name = "numberCard") val numberCard: Int,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "color") val color: String,
    @ColumnInfo(name = "urlArtCrop") val urlArtCrop: String,
    @ColumnInfo(name = "urlArtNormal") val urlArtNormal: String
) {
    companion object{
        const val TABLE_NAME = "carta"
    }
}