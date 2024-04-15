package com.jose.magiccraftapp.datasource.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.jose.magiccraftapp.datasource.model.Deck
import com.jose.magiccraftapp.datasource.repository.ClientDeckRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ClientDeckViewModel @Inject constructor(private val repository: ClientDeckRepository): ViewModel(){

    fun getDecks(idUser: String): LiveData<List<Deck>>{
        return repository.getDecks(idUser)
    }
}