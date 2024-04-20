package com.jose.magiccraftapp.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Event(
    val id: String = "",
    val nombre: String = "",
    val formato: String = "",
    val fecha: String = "",
    val precio: Double = 0.0,
    val aforo: Int = 0,
    var aforoOcupado: Int = 0,
    val urlImagenEvento: String = "",
    var idUsers: MutableList<String> = mutableListOf()
): Parcelable