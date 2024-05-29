package com.jose.magiccraftapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.data.model.CurrentUser
import com.jose.magiccraftapp.data.model.User
import com.jose.magiccraftapp.data.viewmodel.UsuarioViewModel
import com.jose.magiccraftapp.databinding.FragmentAdminChatBinding
import com.jose.magiccraftapp.ui.adapter.AdapterRecyclerViewChat

class AdminChatFragment : Fragment() {

    private var _binding: FragmentAdminChatBinding? = null
    private val binding get() = _binding!!

    private val usuarioViewModel: UsuarioViewModel by viewModels()

    private lateinit var recycler: RecyclerView

    private lateinit var originalUsersList: MutableList<User>

    private lateinit var filteredUsersList: MutableList<User>

    private var nameSearch = ""

    private lateinit var adapter: AdapterRecyclerViewChat

    private lateinit var idUser: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        idUser = CurrentUser.currentUser!!.id

        setUpRecyclerView()
        setUpSearchBar()
    }

    private fun setUpSearchBar() {
        binding.sb.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                nameSearch = newText.orEmpty()
                filterList(nameSearch)
                return true
            }
        })
    }

    private fun filterList(query: String) {
        val filteredList = originalUsersList.filter { user ->
            user.userName.contains(query, ignoreCase = true)
        }
        filteredUsersList.clear()
        filteredUsersList.addAll(filteredList)
        adapter.notifyDataSetChanged()
    }

    private fun setUpRecyclerView() {
        originalUsersList = mutableListOf()
        filteredUsersList = mutableListOf()
        adapter = AdapterRecyclerViewChat(filteredUsersList)
        recycler = binding.rvUsers
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(context)

        // Observar los cambios en los usuarios
        usuarioViewModel.obtainUsersChat(idUser).observe(viewLifecycleOwner) { usuarios ->
            originalUsersList.clear()
            originalUsersList.addAll(usuarios)
            filterList(nameSearch)  // Filtrar la lista inicial
        }
        // Observar los cambios en los usuarios
        usuarioViewModel.obtainUsersChat(idUser).observe(viewLifecycleOwner) { usuarios ->
            originalUsersList.clear()
            originalUsersList.addAll(usuarios)
            filterList(nameSearch)  // Filtrar la lista inicial
        }
        //Click
        adapter.onItemClick = { usuario ->
            handleItemClick(usuario)
        }
        //Long click
    }

    private fun handleItemClick(usuario: User) {
        CurrentUser.currentUserChat = usuario
        Log.e("AdminChatFragment", usuario.toString())
        findNavController().navigate(R.id.action_adminChatFragment_to_adminChattingFragment)
    }
}