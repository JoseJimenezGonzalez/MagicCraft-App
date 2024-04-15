package com.jose.magiccraftapp.datasource.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface ApiRequest {
    fun getEndPoint(): String = "https://api.scryfall.com/"

    fun retrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(getEndPoint())
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
}