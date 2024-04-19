package com.jose.magiccraftapp.data.repository

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.jose.magiccraftapp.data.dao.CartaDao
import com.jose.magiccraftapp.data.database.MagicCraftDatabase
import com.jose.magiccraftapp.data.entity.Carta

class CartaRepository (application: Application) {

    private val cartaDao: CartaDao? = MagicCraftDatabase.getInstance(application)?.cartaDao()
    // Inicialización de DatabaseReference
    var dbRef = FirebaseDatabase.getInstance().reference

    // Inicialización de StorageReference
    var stRef = FirebaseStorage.getInstance().reference

    // Inicialización de FirebaseAuth
    var auth = FirebaseAuth.getInstance()

    fun insert(carta: Carta){
        cartaDao?.insert(carta)
    }


    fun update(carta: Carta){
        cartaDao?.update(carta)
    }


    fun delete(carta: Carta){
        cartaDao?.delete(carta)
    }

    fun insertAll(cartas: MutableList<Carta>){
        cartaDao?.insertAll(cartas)
    }


}