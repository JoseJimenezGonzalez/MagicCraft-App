package com.jose.magiccraftapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.data.model.CurrentUser
import com.jose.magiccraftapp.data.model.News
import com.jose.magiccraftapp.data.viewmodel.NewsViewModel
import com.jose.magiccraftapp.databinding.FragmentClientHomeBinding
import com.jose.magiccraftapp.ui.adapter.AdapterRecyclerViewNews

class ClientHomeFragment : Fragment() {

    private var _binding: FragmentClientHomeBinding? = null
    private val binding get() = _binding!!

    private val newsViewModel: NewsViewModel by viewModels()

    private lateinit var recycler: RecyclerView

    private lateinit var newsList: MutableList<News>

    private lateinit var adapter: AdapterRecyclerViewNews

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Codigo
        setUpButtonOpenUrlRules()
        setUpButtonOpenSetting()
        setUpRecyclerView()
        setUpButtonOpenCalendar()
    }

    private fun handleItem(new: News) {
        CurrentUser.currentNew = new
        findNavController().navigate(R.id.action_clientHomeFragment_to_clientNewViewFragment)
    }

    private fun setUpRecyclerView() {
        newsList = mutableListOf()
        adapter = AdapterRecyclerViewNews(newsList)
        apply {
            recycler = binding.rvNews
            recycler.adapter = adapter
            recycler.layoutManager = LinearLayoutManager(context)
        }
        // Observar los cambios en los mazos
        newsViewModel.getAllNews().observe(viewLifecycleOwner) { news ->
            newsList.clear()
            newsList.addAll(news)
            adapter.notifyDataSetChanged()
        }

        adapter.onItemClick = { new ->
            handleItem(new)
        }

    }

    private fun setUpButtonOpenSetting() {
        binding.ivSettings.setOnClickListener {
            findNavController().navigate(R.id.action_clientHomeFragment_to_clientSettingFragment)
        }
    }

    private fun setUpButtonOpenCalendar(){
        binding.cvCalendar.setOnClickListener {
            findNavController().navigate(R.id.action_clientHomeFragment_to_clientCalendarFragment)
        }
    }

    private fun setUpButtonOpenUrlRules() {
        binding.cvRules.setOnClickListener {
            findNavController().navigate(R.id.action_clientHomeFragment_to_clientViewRulesPdfFragment)
        }

    }

}