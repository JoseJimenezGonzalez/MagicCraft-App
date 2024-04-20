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
import com.google.android.material.card.MaterialCardView
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.data.model.CurrentUser
import com.jose.magiccraftapp.data.model.Message

class AdapterRecyclerViewChatting (private var usersList: MutableList<Message>): RecyclerView.Adapter<AdapterRecyclerViewChatting.ViewHolder>() {

    private lateinit var context: Context


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        context = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = usersList[position]

        if(currentItem.idEmisor == CurrentUser.currentUser!!.idUsuario){
            //Estoy comentando yo, lo que sea del otro lo oculto
            //Yo
            holder.tvNombreMio.text = "Yo"
            holder.tvContenidoMio.text = currentItem.contenido
            holder.tvFechaMio.text = currentItem.fechaHora
            //Otro
            holder.cvImagenOtro.visibility = View.INVISIBLE
            holder.tvNombreOtro.text = ""
            holder.tvContenidoOtro.text = ""
            holder.tvFechaOtro.text = ""
        }else{
            //Está comentando el otro, lo que sea miio lo oculto
            //Yo
            holder.cvImagenMia.visibility = View.INVISIBLE
            holder.tvNombreMio.text = ""
            holder.tvContenidoMio.text = ""
            holder.tvFechaMio.text = ""
            //Otro
            holder.tvContenidoOtro.text = currentItem.contenido
            holder.tvFechaOtro.text = currentItem.fechaHora
        }


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

    }

    override fun getItemCount(): Int = usersList.size

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){

        //Mio
        val cvImagenMia: MaterialCardView = itemView.findViewById(R.id.cvImagenMia)
        val ivImagenMia: ImageView = itemView.findViewById(R.id.ivMia)
        val tvNombreMio: TextView = itemView.findViewById(R.id.tvNombreMio)
        val tvContenidoMio: TextView = itemView.findViewById(R.id.tvContenidoMio)
        val tvFechaMio: TextView = itemView.findViewById(R.id.tvFechaMia)
        //Otro
        val cvImagenOtro: MaterialCardView = itemView.findViewById(R.id.cvImagenOtro)
        val ivImagenOtro: ImageView = itemView.findViewById(R.id.ivOtro)
        val tvNombreOtro: TextView = itemView.findViewById(R.id.tvNombreOtro)
        val tvContenidoOtro: TextView = itemView.findViewById(R.id.tvContenidoOtro)
        val tvFechaOtro: TextView = itemView.findViewById(R.id.tvFechaOtro)
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