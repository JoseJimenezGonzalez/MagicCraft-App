package com.jose.magiccraftapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.databinding.FragmentClientDeckBinding
import com.jose.magiccraftapp.data.model.Card
import com.jose.magiccraftapp.data.model.CurrentUser
import com.jose.magiccraftapp.data.model.Deck
import com.jose.magiccraftapp.data.viewmodel.ClientDeckViewModel
import com.jose.magiccraftapp.ui.activity.ClientCreateDeckActivity
import com.jose.magiccraftapp.ui.adapter.ClientAdapterRecyclerViewDeck
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ClientDeckFragment : Fragment() {

    private var _binding: FragmentClientDeckBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ClientDeckViewModel by viewModels()

    private lateinit var recycler: RecyclerView

    private lateinit var deckList: MutableList<Deck>

    private lateinit var adapter: ClientAdapterRecyclerViewDeck

    private lateinit var idUser: String

    @Inject
    lateinit var dbRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.e("OnCreateView", "1")
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
        }
        // Observar los cambios en los mazos
        viewModel.getDecks(idUser).observe(viewLifecycleOwner) { decks ->
            deckList.clear()
            deckList.addAll(decks)
            adapter.notifyDataSetChanged()
        }
        //Click
        adapter.onItemClick = { deck ->
            handleItemClick(deck)
        }
    }

    private fun handleItemClick(deck: Deck) {
        //Modificamos el mazo actual
        CurrentUser.currentDeck = deck
        findNavController().navigate(R.id.action_clientDeckFragment_to_clientDeckManageFragment)
    }

}