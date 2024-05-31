package com.jose.magiccraftapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.databinding.FragmentClientToolsBinding
import com.jose.magiccraftapp.ui.adapter.ClientAdapterViewPagerTools

class ClientToolsFragment : Fragment() {

    private var _binding: FragmentClientToolsBinding? = null
    private val binding get() = _binding!!

    private val listOptions = listOf("Vidas", "Tormenta", "Dados")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientToolsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Codigo
        val viewPager: ViewPager2 = view.findViewById(R.id.pager)
        val tabLayout: TabLayout = view.findViewById(R.id.tab_layout)

        val adapter = ClientAdapterViewPagerTools(childFragmentManager, lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = "${listOptions[position]}"
        }.attach()
    }

}