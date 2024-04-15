package com.jose.magiccraftapp.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.jose.magiccraftapp.databinding.FragmentClientDeckManageSeeCardsBinding
import com.jose.magiccraftapp.datasource.model.Card
import com.jose.magiccraftapp.datasource.model.CurrentUser
import com.jose.magiccraftapp.ui.adapter.ClientAdapterRecyclerViewCardsDeck
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ClientDeckManageSeeCardsFragment : Fragment() {

    private var _binding: FragmentClientDeckManageSeeCardsBinding? = null
    private val binding get() = _binding!!

    private lateinit var recycler: RecyclerView

    private lateinit var cardList: MutableList<Card>

    private lateinit var adapter: ClientAdapterRecyclerViewCardsDeck

    @Inject
    lateinit var dbRef: DatabaseReference

    @Inject
    lateinit var stoRef: StorageReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientDeckManageSeeCardsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        cardList = CurrentUser.currentDeck!!.cards
        Log.e("Tiene carta el mazo", "tiene ${cardList.size} cartas")
        setUpRecyclerView()
        setUpPopupMenu()
        updateUI()
    }

    private fun updateUI() {
        val nameDeck = CurrentUser.currentDeck!!.nameDeck
        binding.tvNameDeck.text = nameDeck
    }

    private fun setUpPopupMenu() {
        binding.ivPopupMenu.setOnClickListener { view ->
            // Crear una instancia de PopupMenu
            val popupMenu = PopupMenu(requireContext(), view)

            // Inflar el menú desde un recurso de menú
            // popupMenu.menuInflater.inflate(R.menu.your_menu_resource, popupMenu.menu)

            // O agregar elementos de menú de forma programática
            popupMenu.menu.add("Editar Mazo")
            popupMenu.menu.add("Guardar Mazo")

            // Configurar un OnMenuItemClickListener
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.title) {
                    "Editar Mazo" -> {
                        // Manejar clic en "Opción 1"
                        true
                    }
                    "Guardar Mazo" -> {
                        // Manejar clic en "Opción 2"
                        //Modificar el mazo actual
                        modifyCurrentDeckDatabase()
                        true
                    }
                    else -> false
                }
            }

            // Mostrar el menú
            popupMenu.show()
        }
    }

    private fun modifyCurrentDeckDatabase() {
        val idUser = CurrentUser.currentDeck!!.idUserDeck
        val idDeck = CurrentUser.currentDeck!!.idDeck
        val cardsDeck = CurrentUser.currentDeck!!.cards
        dbRef.child("MagicCraft").child("Decks").child(idUser).child(idDeck).child("Cards").setValue(cardsDeck)
        Log.e("Modificar cartas", "exito")
    }


    private fun setUpRecyclerView() {
        adapter = ClientAdapterRecyclerViewCardsDeck(cardList)
        apply {
            recycler = binding.rvCards
            recycler.adapter = adapter
            recycler.layoutManager = LinearLayoutManager(context)
        }
    }

}