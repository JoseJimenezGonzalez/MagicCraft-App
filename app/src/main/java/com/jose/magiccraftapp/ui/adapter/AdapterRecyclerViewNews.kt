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
import com.jose.magiccraftapp.data.model.News

class AdapterRecyclerViewNews (private var newsList: MutableList<News>): RecyclerView.Adapter<AdapterRecyclerViewNews.ViewHolder>() {

    private lateinit var context: Context

    var onItemClick: ((News) -> Unit)? = null


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        context = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = newsList[position]

        holder.textViewTitle.text = currentItem.title
        holder.textViewSubTitle.text = currentItem.subTittle

        val URL: String? = when(currentItem.urlImage){
            "" -> null
            else -> currentItem.urlImage
        }

        Glide.with(context)
            .load(URL)
            .apply(opcionesGlide(context))
            .transition(transicion)
            .into(holder.imageNew)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(currentItem)
        }


    }

    override fun getItemCount(): Int = newsList.size

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){

        val imageNew: ImageView = view.findViewById(R.id.ivImagen)
        val textViewTitle: TextView = view.findViewById(R.id.tvTitle)
        val textViewSubTitle: TextView = view.findViewById(R.id.tvSubTittle)

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