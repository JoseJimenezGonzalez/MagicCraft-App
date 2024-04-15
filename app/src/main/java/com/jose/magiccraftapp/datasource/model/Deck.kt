package com.jose.magiccraftapp.datasource.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Deck(
    val idUserDeck: String = "",
    val idDeck: String = "",
    val nameDeck: String = "",
    val formatDeck: String = "",
    val urlImageFirebase: String = "",
    val cards: MutableList<Card> = mutableListOf()
): Parcelable
