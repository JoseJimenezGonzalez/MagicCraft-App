package com.jose.magiccraftapp.datasource.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class Card(
    var id: String = "",
    var name: String = "",
    var type: String = "",
    var text: String = "",
    var urlArtCrop: String = "",
    var urlArtNormal: String = "",
    var numberCard: Int = 4
): Parcelable
