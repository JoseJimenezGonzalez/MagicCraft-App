package com.jose.magiccraftapp.data.model

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
    var numberCard: Int = 4,
    var cmc: Int = 0
): Parcelable
