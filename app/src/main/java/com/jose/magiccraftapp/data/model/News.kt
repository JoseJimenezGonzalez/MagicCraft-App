package com.jose.magiccraftapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class News(
    var idNew: String = "",
    var urlImage: String = "",
    var urlWeb: String = "",
    var title: String = "",
    var subTittle: String = ""
): Parcelable
