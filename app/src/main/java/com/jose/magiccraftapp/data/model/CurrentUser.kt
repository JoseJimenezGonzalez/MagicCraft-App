package com.jose.magiccraftapp.data.model

class CurrentUser {
    companion object{
        var currentUser: User? = null
        var currentDeck: Deck? = null
        var currentCard: Card? = null
        var currentUserChat: User? = null
        var currentEventChat: Event? = null
        var currentNew: News? = null
    }
}