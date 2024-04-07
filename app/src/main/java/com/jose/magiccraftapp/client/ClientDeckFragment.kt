package com.jose.magiccraftapp.client

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.jose.magiccraftapp.databinding.FragmentClientDeckBinding
import com.jose.magiccraftapp.model.Card
import com.jose.magiccraftapp.model.CurrentUser
import com.jose.magiccraftapp.model.Deck
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ClientDeckFragment : Fragment() {

    private var _binding: FragmentClientDeckBinding? = null
    private val binding get() = _binding!!

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

        Log.e("OnViewCreated", "2")

        idUser = CurrentUser.currentUser!!.id

        binding.fabCreateDeck.setOnClickListener {
            val intent = Intent(context, ClientCreateDeckActivity::class.java)
            startActivity(intent)
        }

        setUpRecyclerView()
        Log.e("Numero elemetos mazo", deckList.size.toString())
    }

    private fun setUpRecyclerView() {
        deckList = mutableListOf()
        dbRef.child("MagicCraft").child("Decks").child(idUser).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                deckList.clear()
                snapshot.children.forEach { deckSnapshot ->
                    val pojoDeck = deckSnapshot.getValue(Deck::class.java)!!
                    // Recuperar las cartas de cada mazo
                    deckSnapshot.child("Cards").children.forEach { cardSnapshot ->
                        val pojoCard = cardSnapshot.getValue(Card::class.java)!!
                        pojoDeck.cards.add(pojoCard)
                    }
                    deckList.add(pojoDeck)
                }
                recycler.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                println(error.message)
            }

        })
        adapter = ClientAdapterRecyclerViewDeck(deckList, findNavController())
        apply {
            recycler = binding.rvDecks
            recycler.adapter = adapter
            recycler.layoutManager = LinearLayoutManager(context)
        }
    }

}