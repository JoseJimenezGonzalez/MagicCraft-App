package com.jose.magiccraftapp.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.jose.magiccraftapp.data.entity.Carta
import com.jose.magiccraftapp.data.repository.CartaRepository

class CartaViewModel (application: Application): AndroidViewModel(application) {

    private val repository = CartaRepository(application)

    fun insert(carta: Carta){
        repository.insert(carta)
    }


    fun update(carta: Carta){
        repository.update(carta)
    }


    fun delete(carta: Carta){
        repository.delete(carta)
    }

    fun insertAll(cartas: MutableList<Carta>){
        repository.insertAll(cartas)
    }
}