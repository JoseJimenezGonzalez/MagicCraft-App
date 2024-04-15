package com.jose.magiccraftapp.datasource.dataclass

import com.google.gson.annotations.SerializedName

data class ImageUrisScryfall(
    @SerializedName("normal")
    val normal: String = "",
    @SerializedName("art_crop")
    val art_crop: String = ""
)
