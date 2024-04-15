package com.jose.magiccraftapp.datasource.dataclass

import com.google.gson.annotations.SerializedName

data class PricesScryfall(
    @SerializedName("usd")
    val usd: String = ""
)
