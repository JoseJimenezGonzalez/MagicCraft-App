package com.jose.magiccraftapp.data.repository

import android.app.Application
import com.google.firebase.database.DatabaseReference
import com.jose.magiccraftapp.data.dao.CartaDao
import com.jose.magiccraftapp.data.database.MagicCraftDatabase
import javax.inject.Inject

class CartaRepository @Inject constructor(private val dbRef: DatabaseReference, application: Application) {

    private val cartaDao: CartaDao? = MagicCraftDatabase.getInstance(application)?.cartaDao()


}