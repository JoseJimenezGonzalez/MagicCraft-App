package com.jose.magiccraftapp.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.FirebaseStorage
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.data.model.CurrentUser
import com.jose.magiccraftapp.data.model.Event
import com.jose.magiccraftapp.data.viewmodel.EventViewModel
import com.jose.magiccraftapp.databinding.FragmentAdminEventManageSeeBinding
import com.jose.magiccraftapp.ui.adapter.AdapterRecyclerViewEvent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AdminEventManageSeeFragment : Fragment() {

    private var _binding: FragmentAdminEventManageSeeBinding? = null
    private val binding get() = _binding!!

    private val eventViewModel: EventViewModel by viewModels()

    private lateinit var recycler: RecyclerView

    private lateinit var eventList: MutableList<Event>

    private lateinit var adapter: AdapterRecyclerViewEvent


    @Inject
    lateinit var dbRef: DatabaseReference

    // Inicialización de StorageReference
    var stRef = FirebaseStorage.getInstance().reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminEventManageSeeBinding.inflate(inflater, container, false)
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
        adapter.onItemLongClick = { event ->
            deleteEvent(event)
        }
    }

    private fun deleteEvent(event: Event) {
        // Las notificaciones no están habilitadas. Muestra un diálogo al usuario.
        val dialogView = layoutInflater.inflate(R.layout.dialog_custom, null)

        val tvTitle = dialogView.findViewById<TextView>(R.id.tvTitle)
        val tvMessage = dialogView.findViewById<TextView>(R.id.tvMessage)
        val btnYes = dialogView.findViewById<Button>(R.id.btnYes)
        val btnNo = dialogView.findViewById<Button>(R.id.btnNo)

        tvTitle.text = "Eliminar evento"
        tvMessage.text = "¿Estás seguro de que quieres eliminar el evento?"

        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        btnYes.setOnClickListener {
            dbRef.child("MagicCraft").child("Events").child(event.id).removeValue()
            stRef.child("MagicCraft").child("Image_Cover_Event").child(event.id).delete()
            eventList.remove(event)
            adapter.notifyDataSetChanged()
            alertDialog.dismiss()
        }

        btnNo.setOnClickListener {
            // El usuario ha rechazado. Cierra el diálogo.
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun handleItemClick(event: Event) {
        //Nos lleva al chat
        CurrentUser.currentEventChat = event
        //Navegar a chatting event
        findNavController().navigate(R.id.action_adminEventFragment_to_adminEventChattingFragment)
    }

}