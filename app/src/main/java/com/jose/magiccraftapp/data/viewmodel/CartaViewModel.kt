package com.jose.magiccraftapp.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.database.DatabaseReference
import com.jose.magiccraftapp.data.repository.CartaRepository
import javax.inject.Inject

class CartaViewModel @Inject constructor(private val dbRef: DatabaseReference, application: Application): AndroidViewModel(application) {

    private val repository = CartaRepository(dbRef, application)
}