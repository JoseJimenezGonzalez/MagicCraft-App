package com.jose.magiccraftapp.datasource.dataclass

import com.google.gson.annotations.SerializedName

data class CardDataclass(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("type_line")
    val type_line: String,
    @SerializedName("oracle_text")
    val oracle_text: String,
    @SerializedName("mana_cost")
    val manaCost: String,
    @SerializedName("cmc")
    val cmc: Int,
    @SerializedName("prices")
    val prices: PricesDataclass,
    @SerializedName("image_uris")
    val image_uris: ImageDataclass,
    @SerializedName("set_name")
    val set_name: String
)
