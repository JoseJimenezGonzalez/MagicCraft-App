package com.jose.magiccraftapp.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.card.MaterialCardView
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.data.model.CurrentUser
import com.jose.magiccraftapp.data.model.Event

class AdapterRecyclerViewEvent (private var eventList: MutableList<Event>): RecyclerView.Adapter<AdapterRecyclerViewEvent.ViewHolder>() {

    private lateinit var context: Context

    var onItemClick: ((Event) -> Unit)? = null
    var onBtnClick: ((Event) -> Unit)? = null
    var onItemLongClick: ((Event) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event_admin, parent, false)
        context = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = eventList[position]

        holder.tvNombreEvento.text = "Nombre del torneo: ${currentItem.nombre}"
        holder.tvFormatoEvento.text = "Formato del torneo: ${currentItem.formato}"
        holder.tvFechaEvento.text = "Fecha del evento: ${currentItem.fecha}"
        holder.tvPrecio.text = "Precio del torneo: ${currentItem.precio} euros"
        holder.tvAforoMaximo.text = "Aforo máximo permitido: ${currentItem.aforo}"
        holder.tvAforoOcupado.text = "Aforo ocupado: ${currentItem.aforoOcupado}"


        val URL: String? = when(currentItem.urlImagenEvento){
            "" -> null
            else -> currentItem.urlImagenEvento
        }

        Glide.with(context)
            .load(URL)
            .apply(opcionesGlide(context))
            .transition(transicion)
            .into(holder.ivFoto)


        if(currentItem.idUsers.contains(CurrentUser.currentUser!!.id)){
            holder.boton.visibility = View.GONE
            holder.cvEvent.setCardBackgroundColor(ContextCompat.getColor(context, R.color.verde_background_event))
            Log.e("true", "true")
        }else if(currentItem.aforoOcupado == currentItem.aforo){
            holder.boton.visibility = View.GONE
            holder.cvEvent.setCardBackgroundColor(ContextCompat.getColor(context, R.color.rojo_background_event))
        }else if(CurrentUser.currentUser!!.typeUser == "administrador"){
            holder.boton.visibility = View.GONE
            holder.cvEvent.setCardBackgroundColor(ContextCompat.getColor(context, R.color.gris))
        }else{
            holder.boton.visibility = View.VISIBLE
            holder.cvEvent.setCardBackgroundColor(ContextCompat.getColor(context, R.color.gris))
        }
        //Tratar el click sobre un elemento del recycler view
        //Vamos al chat de este evento
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(currentItem)
        }
        //Nos apuntamos al torneo
        holder.boton.setOnClickListener {
            onBtnClick?.invoke(currentItem)
        }
        //Longclick
        holder.itemView.setOnLongClickListener {
            onItemLongClick?.invoke(currentItem)
            true
        }
    }

    override fun getItemCount(): Int = eventList.size

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){

        val ivFoto: ImageView = itemView.findViewById(R.id.ivFotoEvento)
        val tvNombreEvento: TextView = itemView.findViewById(R.id.tvNombreEvento)
        val tvFormatoEvento: TextView = itemView.findViewById(R.id.tvFormatoEvento)
        val tvFechaEvento: TextView = itemView.findViewById(R.id.tvFechaEvento)
        val tvPrecio: TextView = itemView.findViewById(R.id.tvPrecioEvento)
        val tvAforoMaximo: TextView = itemView.findViewById(R.id.tvAforoMaximo)
        val tvAforoOcupado: TextView = itemView.findViewById(R.id.tvAforoOcupado)
        val boton: Button = itemView.findViewById(R.id.btnApuntarseEvento)
        val cvEvent: MaterialCardView = itemView.findViewById(R.id.cvEvent)
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