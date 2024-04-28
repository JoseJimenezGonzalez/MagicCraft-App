package com.jose.magiccraftapp.ui.adapter

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
import com.jose.magiccraftapp.data.model.Card

class ClientAdapterRecyclerViewCardsDeck (private var cardList: MutableList<Card>): RecyclerView.Adapter<ClientAdapterRecyclerViewCardsDeck.ViewHolder>() {

    private lateinit var context: Context

    var onItemClick: ((Card) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card_deck, parent, false)
        context = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = cardList[position]

        holder.textViewNameCard.text = currentItem.name
        holder.textViewNumberCard.text = currentItem.numberCard.toString()

        val URL: String? = when(currentItem.urlArtCrop){
            "" -> null
            else -> currentItem.urlArtCrop
        }

        Glide.with(context)
            .load(URL)
            .apply(opcionesGlide(context))
            .transition(transicion)
            .into(holder.imageCard)

        //Tratar el click sobre un elemento del recycler view
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(currentItem)
        }
        holder.imageViewMinus.setOnClickListener {
            currentItem.numberCard--
            if (currentItem.numberCard == 0) {
                cardList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, cardList.size)
            } else {
                holder.textViewNumberCard.text = currentItem.numberCard.toString()
            }
        }
        holder.imageViewPlus.setOnClickListener {
            currentItem.numberCard++
            holder.textViewNumberCard.text = currentItem.numberCard.toString()
        }
    }

    override fun getItemCount(): Int = cardList.size

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){

        val textViewNameCard: TextView = view.findViewById(R.id.tvNameCard)
        val imageCard: ImageView = view.findViewById(R.id.ivImageCard)
        val imageViewMinus: ImageView = view.findViewById(R.id.ivMinus)
        val imageViewPlus: ImageView = view.findViewById(R.id.ivPlus)
        val textViewNumberCard: TextView = view.findViewById(R.id.tvNumberCard)
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