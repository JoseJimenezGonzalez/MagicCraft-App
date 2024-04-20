package com.jose.magiccraftapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MessageEvent(
    var idMensaje: String = "",
    var idEmisor: String = "",
    var nombreEmisor: String = "",
    var urlImagenEmisor: String = "",
    var contenido: String = "",
    var fechaHora: String = ""
): Parcelable