package com.jose.magiccraftapp.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.data.model.CurrentUser
import com.jose.magiccraftapp.data.model.Event
import com.jose.magiccraftapp.data.viewmodel.EventViewModel
import com.jose.magiccraftapp.databinding.FragmentAdminCalendarEventBinding
import com.jose.magiccraftapp.databinding.FragmentClientCalendarEventBinding
import com.jose.magiccraftapp.ui.adapter.AdapterRecyclerViewEvent

class AdminCalendarEventFragment : Fragment() {

    private var _binding: FragmentAdminCalendarEventBinding? = null

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

    var fechaString = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminCalendarEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Recojo la fecha
        fechaString = arguments?.getString("fechaString")!!
        Log.e("AdminCalendarEventFragment", fechaString)
        //Actualizamos la ui
        binding.tvTitle.text = "Listado de eventos para el dia ${fechaString}"
        //Recycler view con los eventos que estan previstos para esa fecha
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
            //filtro los eventos por fecha

            eventList.clear()
            eventList.addAll(events.filter { event -> event.fecha == fechaString })
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
        idUsers.add(CurrentUser.currentUser!!.id)
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
        CurrentUser.currentEventChat = event
        //Navegar a chatting event
        findNavController().navigate(R.id.action_adminCalendarEventFragment_to_adminEventChattingFragment)
    }

}