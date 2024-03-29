package com.jose.magiccraftapp.data

import com.jose.magiccraftapp.model.CardScryfall
import retrofit2.http.GET
import retrofit2.http.Query

interface CardApiService {
    @GET("named")
    suspend fun getCardByName(@Query("exact") name: String): CardScryfall
}