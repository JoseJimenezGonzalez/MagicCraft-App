package com.jose.magiccraftapp.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.jose.magiccraftapp.data.entity.Mazo
import com.jose.magiccraftapp.data.model.Deck
import com.jose.magiccraftapp.data.repository.MazoRepository

class MazoViewModel (application: Application): AndroidViewModel(application) {

    private val repository = MazoRepository(application)

    fun getDecks(idUser: String): LiveData<List<Deck>> {
        return repository.getDecks(idUser)
    }

    fun insert(mazo: Mazo){
        repository.insert(mazo)
    }


    fun update(mazo: Mazo){
        repository.insert(mazo)
    }


    fun delete(mazo: Mazo){
        repository.insert(mazo)
    }

    fun insertAll(mazos: MutableList<Mazo>){
        repository.insertAll(mazos)
    }

}