package com.jose.magiccraftapp.ui.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.FirebaseStorage
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.data.model.CurrentUser
import com.jose.magiccraftapp.data.model.Deck
import com.jose.magiccraftapp.data.viewmodel.MazoViewModel
import com.jose.magiccraftapp.databinding.FragmentClientDeckBinding
import com.jose.magiccraftapp.ui.activity.ClientCreateDeckActivity
import com.jose.magiccraftapp.ui.adapter.ClientAdapterRecyclerViewDeck
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ClientDeckFragment : Fragment() {

    private var _binding: FragmentClientDeckBinding? = null
    private val binding get() = _binding!!

    private val mazoViewModel: MazoViewModel by viewModels()

    private lateinit var recycler: RecyclerView

    private lateinit var deckList: MutableList<Deck>

    private lateinit var adapter: ClientAdapterRecyclerViewDeck

    private lateinit var idUser: String

    @Inject
    lateinit var dbRef: DatabaseReference

    // Inicialización de StorageReference
    var stRef = FirebaseStorage.getInstance().reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientDeckBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        idUser = CurrentUser.currentUser!!.id

        binding.fabCreateDeck.setOnClickListener {
            val intent = Intent(context, ClientCreateDeckActivity::class.java)
            startActivity(intent)
        }

        setUpRecyclerView()

    }

    private fun setUpRecyclerView() {
        deckList = mutableListOf()
        adapter = ClientAdapterRecyclerViewDeck(deckList)
        apply {
            recycler = binding.rvDecks
            recycler.adapter = adapter
            recycler.layoutManager = LinearLayoutManager(context)

            // Define el número de columnas que quieres en tu cuadrícula
            val numberOfColumns = 2

            // Crea una instancia de GridLayoutManager
            val gridLayoutManager = GridLayoutManager(context, numberOfColumns)

            // Establece el GridLayoutManager en tu RecyclerView
            recycler.layoutManager = gridLayoutManager
        }
        // Observar los cambios en los mazos
        mazoViewModel.getDecks(idUser).observe(viewLifecycleOwner) { decks ->
            deckList.clear()
            deckList.addAll(decks)
            adapter.notifyDataSetChanged()
        }
        //Click
        adapter.onItemClick = { deck ->
            handleItemClick(deck)
        }
        adapter.onItemLongClick = { deck ->
            // Las notificaciones no están habilitadas. Muestra un diálogo al usuario.
            val dialogView = layoutInflater.inflate(R.layout.dialog_custom, null)

            val tvTitle = dialogView.findViewById<TextView>(R.id.tvTitle)
            val tvMessage = dialogView.findViewById<TextView>(R.id.tvMessage)
            val btnYes = dialogView.findViewById<Button>(R.id.btnYes)
            val btnNo = dialogView.findViewById<Button>(R.id.btnNo)

            tvTitle.text = "Eliminar mazo"
            tvMessage.text = "¿Estás seguro de que quieres eliminar el mazo?"

            val alertDialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create()

            btnYes.setOnClickListener {
                dbRef.child("MagicCraft").child("Decks").child(deck.idUserDeck).child(deck.idDeck).removeValue()
                stRef.child("MagicCraft").child("Image_Cover_Deck").child(deck.idUserDeck).child(deck.idDeck).delete()
                deckList.remove(deck)
                adapter.notifyDataSetChanged()
                alertDialog.dismiss()
            }

            btnNo.setOnClickListener {
                // El usuario ha rechazado. Cierra el diálogo.
                alertDialog.dismiss()
            }

            alertDialog.show()
        }
    }

    private fun handleItemClick(deck: Deck) {
        //Modificamos el mazo actual
        CurrentUser.currentDeck = deck
        findNavController().navigate(R.id.action_clientDeckFragment_to_clientDeckManageFragment)
    }

}