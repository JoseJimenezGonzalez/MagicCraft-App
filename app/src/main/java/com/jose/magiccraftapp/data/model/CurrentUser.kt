package com.jose.magiccraftapp.data.model

import com.jose.magiccraftapp.data.entity.Usuario

class CurrentUser {
    companion object{
        var currentUser: Usuario? = null
        var currentDeck: Deck? = null
        var currentCard: Card? = null
        var currentUserChat: User? = null
        var currentEventChat: Event? = null
        var currentNew: News? = null
    }
}