package com.jose.magiccraftapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.jose.magiccraftapp.data.dao.MazoDao
import com.jose.magiccraftapp.data.database.MagicCraftDatabase
import com.jose.magiccraftapp.data.entity.Mazo
import com.jose.magiccraftapp.data.model.Card
import com.jose.magiccraftapp.data.model.Deck

class MazoRepository (application: Application) {
    val mazoDao: MazoDao? = MagicCraftDatabase.getInstance(application)?.mazoDao()

    // Inicialización de DatabaseReference
    var dbRef = FirebaseDatabase.getInstance().reference

    // Inicialización de StorageReference
    var stRef = FirebaseStorage.getInstance().reference

    // Inicialización de FirebaseAuth
    var auth = FirebaseAuth.getInstance()

    fun getDecks(idUser: String): LiveData<List<Deck>> {
        val mutableData = MutableLiveData<List<Deck>>()
        dbRef.child("MagicCraft").child("Decks").child(idUser).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val deckList = mutableListOf<Deck>()
                snapshot.children.forEach { deckSnapshot ->
                    val pojoDeck = deckSnapshot.getValue(Deck::class.java)!!
                    // Recuperar las cartas de cada mazo
                    deckSnapshot.child("Cards").children.forEach { cardSnapshot ->
                        val pojoCard = cardSnapshot.getValue(Card::class.java)!!
                        pojoDeck.cards.add(pojoCard)
                    }
                    deckList.add(pojoDeck)
                }
                mutableData.value = deckList
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
        return mutableData
    }

    fun insert(mazo: Mazo){
        mazoDao?.insert(mazo)
    }


    fun update(mazo: Mazo){
        mazoDao?.update(mazo)
    }


    fun delete(mazo: Mazo){
        mazoDao?.delete(mazo)
    }

    fun insertAll(mazos: MutableList<Mazo>){
        mazoDao?.insertAll(mazos)
    }

}