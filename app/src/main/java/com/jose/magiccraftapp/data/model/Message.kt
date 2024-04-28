package com.jose.magiccraftapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Message(
    var idMessage: String = "",
    var idChat: String = "",
    var idEmisor: String = "",
    var idReceptor: String = "",
    var nombreEmisor: String = "",
    var nombreReceptor: String = "",
    var imagenEmisor: String = "",
    var imagenReceptor: String = "",
    var contenido: String = "",
    var fechaHora: String = ""
): Parcelable
