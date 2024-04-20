package com.jose.magiccraftapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.data.model.User

class AdapterRecyclerViewChat (private var usersList: MutableList<User>): RecyclerView.Adapter<AdapterRecyclerViewChat.ViewHolder>() {

    private lateinit var context: Context

    var onItemClick: ((User) -> Unit)? = null
    var onLongItemClick: ((User) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        context = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = usersList[position]

        holder.textViewNameUser.text = currentItem.name


        //val URL: String? = when(currentItem.urlImageFirebase){
        //    "" -> null
        //    else -> currentItem.urlImageFirebase
        //}

        //Glide.with(context)
        //    .load(URL)
        //    .apply(opcionesGlide(context))
        //    .transition(transicion)
        //    .into(holder.imageUser)

        //Tratar el click sobre un elemento del recycler view
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(currentItem)
        }
        holder.itemView.setOnLongClickListener {
            onLongItemClick?.invoke(currentItem)
            true
        }
    }

    override fun getItemCount(): Int = usersList.size

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){

        val textViewNameUser: TextView = view.findViewById(R.id.tvUserName)
        val imageUser: ImageView = view.findViewById(R.id.ivUser)
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