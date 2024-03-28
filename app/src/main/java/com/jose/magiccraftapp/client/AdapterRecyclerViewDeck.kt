package com.jose.magiccraftapp.client

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.model.Deck

class AdapterRecyclerViewDeck(private var deckList: MutableList<Deck>): RecyclerView.Adapter<AdapterRecyclerViewDeck.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterRecyclerViewDeck.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_deck, parent, false)
        context = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterRecyclerViewDeck.ViewHolder, position: Int) {
        val currentItem = deckList[position]

        holder.textViewNameDeck.text = currentItem.nameDeck
        holder.textViewFormatDeck.text = currentItem.formatDeck

        val URL: String? = when(currentItem.urlImageFirebase){
            "" -> null
            else -> currentItem.urlImageFirebase
        }

        Glide.with(context)
            .load(URL)
            .apply(opcionesGlide(context))
            .transition(transicion)
            .into(holder.imageDeck)
    }

    override fun getItemCount(): Int = deckList.size

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){

        val textViewNameDeck: TextView = view.findViewById(R.id.tvDeckName)
        val textViewFormatDeck: TextView = view.findViewById(R.id.tvDeckFormat)
        val imageDeck: ImageView = view.findViewById(R.id.ivDeck)
    }

    private val transicion = DrawableTransitionOptions.withCrossFade(500)

    private fun opcionesGlide(context: Context): RequestOptions {
        return RequestOptions()
            .placeholder(animationLoading(context))

    }

    private fun animationLoading(context: Context): CircularProgressDrawable {
        val animation = CircularProgressDrawable(context)
        animation.strokeWidth = 5f
        animation.centerRadius = 30f
        animation.start()
        return animation
    }
}