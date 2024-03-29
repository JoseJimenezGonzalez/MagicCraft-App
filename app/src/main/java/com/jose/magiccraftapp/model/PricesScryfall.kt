package com.jose.magiccraftapp.model

import com.google.gson.annotations.SerializedName

data class PricesScryfall(
    @SerializedName("usd")
    val usd: String = ""
)
