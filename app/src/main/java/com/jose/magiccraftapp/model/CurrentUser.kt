package com.jose.magiccraftapp.model

class CurrentUser {
    companion object{
        var currentUser: User? = null
        var currentDeck: Deck? = null
        var currentCard: Card? = null
    }
}