package com.jose.magiccraftapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.DatabaseReference
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.data.model.Deck
import com.jose.magiccraftapp.data.viewmodel.MazoViewModel
import com.jose.magiccraftapp.databinding.FragmentAdminNewsBinding
import com.jose.magiccraftapp.databinding.FragmentClientDeckBinding
import com.jose.magiccraftapp.ui.adapter.AdminAdapterViewPagerEvent
import com.jose.magiccraftapp.ui.adapter.AdminAdapterViewPagerNews
import com.jose.magiccraftapp.ui.adapter.ClientAdapterRecyclerViewDeck
import javax.inject.Inject

class AdminNewsFragment : Fragment() {

    private var _binding: FragmentAdminNewsBinding? = null

    private val binding get() = _binding!!

    private val listOptions = listOf("Añadir noticia", "Ver noticias")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Codigo
        val viewPager: ViewPager2 = view.findViewById(R.id.pager)
        val tabLayout: TabLayout = view.findViewById(R.id.tab_layout)

        val adapter = AdminAdapterViewPagerNews(childFragmentManager, lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = "${listOptions[position]}"
        }.attach()
    }
}