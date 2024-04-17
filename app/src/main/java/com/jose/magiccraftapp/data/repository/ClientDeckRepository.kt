package com.jose.magiccraftapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.jose.magiccraftapp.data.model.Card
import com.jose.magiccraftapp.data.model.Deck
import javax.inject.Inject

class ClientDeckRepository @Inject constructor(private val dbRef: DatabaseReference) {

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
}