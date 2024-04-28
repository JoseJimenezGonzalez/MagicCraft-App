package com.jose.magiccraftapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.jose.magiccraftapp.data.model.CurrentUser
import com.jose.magiccraftapp.data.model.Message
import com.jose.magiccraftapp.data.viewmodel.MessageViewModel
import com.jose.magiccraftapp.databinding.FragmentClientChattingBinding
import com.jose.magiccraftapp.ui.adapter.AdapterRecyclerViewChatting
import java.text.SimpleDateFormat
import java.util.Calendar

class ClientChattingFragment : Fragment() {

    private var _binding: FragmentClientChattingBinding? = null
    private val binding get() = _binding!!

    private var idChat = ""

    // Inicialización de DatabaseReference
    var dbRef = FirebaseDatabase.getInstance().reference

    // Inicialización de StorageReference
    var stRef = FirebaseStorage.getInstance().reference

    // Inicialización de FirebaseAuth
    var auth = FirebaseAuth.getInstance()

    private val messageViewModel: MessageViewModel by viewModels()

    private lateinit var recycler: RecyclerView

    private lateinit var messageList: MutableList<Message>

    private lateinit var adapter: AdapterRecyclerViewChatting


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientChattingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Codigo

        val idCurrentUser = CurrentUser.currentUser!!.id

        val idUserChat = CurrentUser.currentUserChat!!.id

        Log.e("ClientChattingFragmentU", CurrentUser.currentUser.toString())
        Log.e("ClientChattingFragmentC", CurrentUser.currentUserChat.toString())

        getChatId(idCurrentUser, idUserChat)
        setUpButtonSendChat()
        setUpRecyclerView()
    }

    private fun setUpButtonSendChat() {
        binding.btnChat.setOnClickListener {
            val mensajeEnviado = binding.etChat.text.toString().trim()
            if(mensajeEnviado.isNotBlank()){
                val hoy = Calendar.getInstance()
                val formateador = SimpleDateFormat("YYYY-MM-dd HH:mm:ss")
                val fechaHora = formateador.format(hoy.getTime())
                val idMensaje = dbRef.child("MagicCraft").child("Chat").child(idChat).push().key!!
                val mensaje = Message(
                    idMensaje,
                    idChat,
                    CurrentUser.currentUser!!.id,
                    CurrentUser.currentUserChat!!.id,
                    CurrentUser.currentUser!!.name,
                    CurrentUser.currentUserChat!!.name,
                    CurrentUser.currentUser!!.urlImageFirebase,
                    CurrentUser.currentUserChat!!.urlImageFirebase,
                    mensajeEnviado,
                    fechaHora
                )
                Log.e("ClientChattingFragment", mensaje.toString())
                dbRef.child("MagicCraft").child("Chat").child(idChat).child(idMensaje).setValue(mensaje)
                binding.etChat.text.clear()
            }
        }
    }
    private fun getChatId(idUser1: String, idUser2: String){
        val userIds = listOf(idUser1, idUser2).sorted()
        idChat = userIds.joinToString("")
    }

    private fun setUpRecyclerView() {
        messageList = mutableListOf()
        adapter = AdapterRecyclerViewChatting(messageList)
        apply {
            recycler = binding.rvChat
            recycler.adapter = adapter
            recycler.layoutManager = LinearLayoutManager(context)
        }
        // Observar los cambios en los mazos
        messageViewModel.getMessages(idChat).observe(viewLifecycleOwner) { messages ->
            messageList.clear()
            messageList.addAll(messages)
            adapter.notifyDataSetChanged()
        }

    }

}