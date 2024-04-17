package com.jose.magiccraftapp.datasource.retrofit

import com.jose.magiccraftapp.datasource.dataclass.CardDataclass
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
    @GET("cards/named")
    suspend fun getCardByName(@Query("exact") name: String): CardDataclass
}