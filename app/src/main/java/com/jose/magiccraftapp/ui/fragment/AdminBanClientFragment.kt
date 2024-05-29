package com.jose.magiccraftapp.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jose.magiccraftapp.data.model.CurrentUser
import com.jose.magiccraftapp.data.model.User
import com.jose.magiccraftapp.data.viewmodel.UsuarioViewModel
import com.jose.magiccraftapp.databinding.FragmentAdminBanClientBinding
import com.jose.magiccraftapp.ui.adapter.AdapterRecyclerViewChat


class AdminBanClientFragment : Fragment() {

    private var _binding: FragmentAdminBanClientBinding? = null
    private val binding get() = _binding!!

    private val usuarioViewModel: UsuarioViewModel by viewModels()

    private lateinit var recycler: RecyclerView

    private lateinit var originalUsersList: MutableList<User>

    private lateinit var filteredUsersList: MutableList<User>

    private lateinit var adapter: AdapterRecyclerViewChat

    private lateinit var idUser: String

    private var nameSearch = ""

    private lateinit var auth: FirebaseAuth

    lateinit var dbRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminBanClientBinding.inflate(inflater, container, false)
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
        recycler = binding.rvClients
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(context)

        // Observar los cambios en los usuarios
        usuarioViewModel.obtainUsersChat(idUser).observe(viewLifecycleOwner) { usuarios ->
            originalUsersList.clear()
            originalUsersList.addAll(usuarios)
            filterList(nameSearch)  // Filtrar la lista inicial
        }

        // Long click
        adapter.onLongItemClick = { usuario ->
            handleItemClick(usuario)
        }
    }


    private fun handleItemClick(usuario: User) {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Eliminar usuario")
            setMessage("¿Estás seguro de que quieres eliminar a ${usuario.userName}?")
            setPositiveButton("Sí") { _, _ ->
                // Elimino al usuario de la base de datos
                eliminarAuth(usuario)
            }
            setNegativeButton("No", null)
        }.show()
    }

    private fun eliminarDeBaseDatos(usuario: User) {
        dbRef = FirebaseDatabase.getInstance().getReference()
        dbRef.child("MagicCraft").child("Users").child(usuario.id).removeValue().addOnSuccessListener {
            Toast.makeText(context, "Usuario eliminado correctamente de la base de datos", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e ->
            Toast.makeText(context, "Error al eliminar usuario", Toast.LENGTH_SHORT).show()
        }
    }

    private fun eliminarAuth(usuario: User) {
        auth = Firebase.auth
        auth.signInWithEmailAndPassword(usuario.mail, usuario.password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                val user = auth.currentUser
                user.let {
                    // Elimina al usuario
                    it!!.delete()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(context, "Usuario eliminado correctamente", Toast.LENGTH_SHORT).show()
                                eliminarDeBaseDatos(usuario)
                            } else {
                                Toast.makeText(context, "No se ha podido eliminar al usuario", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }else{
                Toast.makeText(context, "No se ha podido eliminar al usuario", Toast.LENGTH_SHORT).show()
            }
        }
    }

}