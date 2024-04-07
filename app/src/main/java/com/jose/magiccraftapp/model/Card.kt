package com.jose.magiccraftapp.model

data class Card(
    var id: String = "",
    var name: String = "",
    var type: String = "",
    var text: String = "",
    var urlArtCrop: String = "",
    var urlArtNormal: String = "",
    var numberCard: Int = 4
)
