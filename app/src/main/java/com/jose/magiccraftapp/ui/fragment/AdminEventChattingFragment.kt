package com.jose.magiccraftapp.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.jose.magiccraftapp.data.model.CurrentUser
import com.jose.magiccraftapp.data.model.MessageEvent
import com.jose.magiccraftapp.data.viewmodel.MessageViewModel
import com.jose.magiccraftapp.databinding.FragmentAdminEventChattingBinding
import com.jose.magiccraftapp.ui.adapter.AdapterRecyclerViewEventChatting
import java.text.SimpleDateFormat
import java.util.Calendar

class AdminEventChattingFragment : Fragment() {

    private var _binding: FragmentAdminEventChattingBinding? = null

    private val binding get() = _binding!!

    // Inicialización de DatabaseReference
    var dbRef = FirebaseDatabase.getInstance().reference

    // Inicialización de StorageReference
    var stRef = FirebaseStorage.getInstance().reference

    // Inicialización de FirebaseAuth
    var auth = FirebaseAuth.getInstance()

    private val messageViewModel: MessageViewModel by viewModels()

    private lateinit var recycler: RecyclerView

    private lateinit var messageList: MutableList<MessageEvent>

    private lateinit var adapter: AdapterRecyclerViewEventChatting

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminEventChattingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Codigo
        //Codigo

        val URL: String? = when(CurrentUser.currentEventChat!!.urlImagenEvento){
            "" -> null
            else -> CurrentUser.currentEventChat!!.urlImagenEvento
        }

        context?.let {
            Glide.with(it)
                .load(URL)
                .apply(opcionesGlide(requireContext()))
                .transition(transicion)
                .into(binding.ivImageEvent)
        }

        binding.tvNombreEvento.text = CurrentUser.currentEventChat!!.nombre

        setUpButtonSendMessage()
        setUpRecyclerView()
    }

    private fun setUpButtonSendMessage() {
        binding.btnChat.setOnClickListener {
            val mensajeEnviado = binding.etChat.text.toString().trim()
            if(mensajeEnviado.isNotBlank()){
                val hoy = Calendar.getInstance()
                val formateador = SimpleDateFormat("YYYY-MM-dd HH:mm:ss")
                val fechaHora = formateador.format(hoy.getTime())
                val idMensaje = dbRef.child("MagicCraft").child("Events").child(CurrentUser.currentEventChat!!.id).child("Chat").push().key!!
                val mensajeEvento = MessageEvent(
                    idMensaje,
                    CurrentUser.currentUser!!.id,
                    CurrentUser.currentUser!!.name,
                    CurrentUser.currentUser!!.urlImageFirebase,
                    mensajeEnviado,
                    fechaHora
                )
                dbRef.child("MagicCraft").child("Events").child(CurrentUser.currentEventChat!!.id).child("Chat").child(idMensaje).setValue(mensajeEvento)
                binding.etChat.text.clear()
            }
        }
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

    private fun setUpRecyclerView() {
        messageList = mutableListOf()
        adapter = AdapterRecyclerViewEventChatting(messageList)
        apply {
            recycler = binding.rvEventChat
            recycler.adapter = adapter
            recycler.layoutManager = LinearLayoutManager(context)
        }
        // Observar los cambios en los mazos
        messageViewModel.getMessagesOfEvent(CurrentUser.currentEventChat!!.id).observe(viewLifecycleOwner) { messages ->
            messageList.clear()
            messageList.addAll(messages)
            adapter.notifyDataSetChanged()
        }

    }

}