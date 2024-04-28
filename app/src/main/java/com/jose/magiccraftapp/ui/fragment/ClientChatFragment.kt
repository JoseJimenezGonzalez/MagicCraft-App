package com.jose.magiccraftapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.data.model.CurrentUser
import com.jose.magiccraftapp.data.model.User
import com.jose.magiccraftapp.data.viewmodel.UsuarioViewModel
import com.jose.magiccraftapp.databinding.FragmentClientChatBinding
import com.jose.magiccraftapp.ui.adapter.AdapterRecyclerViewChat

class ClientChatFragment : Fragment() {

    private var _binding: FragmentClientChatBinding? = null
    private val binding get() = _binding!!

    private val usuarioViewModel: UsuarioViewModel by viewModels()

    private lateinit var recycler: RecyclerView

    private lateinit var usersList: MutableList<User>

    private lateinit var adapter: AdapterRecyclerViewChat

    private lateinit var idUser: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        idUser = CurrentUser.currentUser!!.id

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        usersList = mutableListOf()
        adapter = AdapterRecyclerViewChat(usersList)
        apply {
            recycler = binding.rvUsers
            recycler.adapter = adapter
            recycler.layoutManager = LinearLayoutManager(context)
        }
        // Observar los cambios en los mazos
        usuarioViewModel.obtainUsersChat(idUser).observe(viewLifecycleOwner) { usuarios ->
            usersList.clear()
            usersList.addAll(usuarios)
            adapter.notifyDataSetChanged()
        }
        //Click
        adapter.onItemClick = { usuario ->
            handleItemClick(usuario)
        }
        //Long click
    }

    private fun handleItemClick(usuario: User) {
        CurrentUser.currentUserChat = usuario
        Log.e("ClientChatFragment", usuario.toString())
        findNavController().navigate(R.id.action_clientChatFragment_to_clientChattingFragment)
    }

}