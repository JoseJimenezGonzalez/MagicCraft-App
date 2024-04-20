package com.jose.magiccraftapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jose.magiccraftapp.data.model.Event
import com.jose.magiccraftapp.data.viewmodel.EventViewModel
import com.jose.magiccraftapp.databinding.FragmentAdminEventManageSeeBinding
import com.jose.magiccraftapp.ui.adapter.AdapterRecyclerViewEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminEventManageSeeFragment : Fragment() {

    private var _binding: FragmentAdminEventManageSeeBinding? = null
    private val binding get() = _binding!!

    private val eventViewModel: EventViewModel by viewModels()

    private lateinit var recycler: RecyclerView

    private lateinit var eventList: MutableList<Event>

    private lateinit var adapter: AdapterRecyclerViewEvent

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
        adapter.onBtnClick = { event ->
            handleBtnClick(event)
        }
    }

    private fun handleBtnClick(event: Event) {

    }

    private fun handleItemClick(event: Event) {

    }

}