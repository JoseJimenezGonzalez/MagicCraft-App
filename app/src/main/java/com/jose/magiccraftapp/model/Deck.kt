package com.jose.magiccraftapp.model

data class Deck(
    val idUserDeck: String = "",
    val idDeck: String = "",
    val nameDeck: String = "",
    val formatDeck: String = "",
    val urlImageFirebase: String = "",
    val cards: MutableList<CardScryfall> = mutableListOf()
)
