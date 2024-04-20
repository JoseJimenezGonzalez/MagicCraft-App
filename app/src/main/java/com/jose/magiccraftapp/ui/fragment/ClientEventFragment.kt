package com.jose.magiccraftapp.ui.fragment

import android.os.Bundle
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
import com.jose.magiccraftapp.data.model.Event
import com.jose.magiccraftapp.data.viewmodel.EventViewModel
import com.jose.magiccraftapp.databinding.FragmentClientEventBinding
import com.jose.magiccraftapp.ui.adapter.AdapterRecyclerViewEvent

class ClientEventFragment : Fragment() {

    private var _binding: FragmentClientEventBinding? = null
    private val binding get() = _binding!!

    private val eventViewModel: EventViewModel by viewModels()

    private lateinit var recycler: RecyclerView

    private lateinit var eventList: MutableList<Event>

    private lateinit var adapter: AdapterRecyclerViewEvent

    // Inicialización de DatabaseReference
    var dbRef = FirebaseDatabase.getInstance().reference

    // Inicialización de StorageReference
    var stRef = FirebaseStorage.getInstance().reference

    // Inicialización de FirebaseAuth
    var auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Codigo
        setUpRecyclerView()

    }

    private fun setUpRecyclerView() {
        eventList = mutableListOf()
        adapter = AdapterRecyclerViewEvent(eventList)
        apply {
            recycler = binding.rvEvents
            recycler.adapter = adapter
            recycler.layoutManager = LinearLayoutManager(context)
        }
        // Observar los cambios en los mazos
        eventViewModel.getAllEvents().observe(viewLifecycleOwner) { events ->
            eventList.clear()
            eventList.addAll(events)
            adapter.notifyDataSetChanged()
        }
        //Click
        adapter.onItemClick = { event ->
            handleItemClick(event)
        }
        adapter.onBtnClick = { event ->
            handleBtnClick(event)
        }
    }

    private fun handleBtnClick(event: Event) {
        //Obtener la lista de los usuarios apuntados al evento
        val idUsers = event.idUsers
        idUsers.add(CurrentUser.currentUser!!.idUsuario)
        //Se apunta al evento
        dbRef.child("MagicCraft").child("Events").child(event.id).setValue(
            Event(
                event.id,
                event.nombre,
                event.formato,
                event.fecha,
                event.precio,
                event.aforo,
                idUsers.size,
                event.urlImagenEvento,
                idUsers
            )
        )
    }

    private fun handleItemClick(event: Event) {

    }

}