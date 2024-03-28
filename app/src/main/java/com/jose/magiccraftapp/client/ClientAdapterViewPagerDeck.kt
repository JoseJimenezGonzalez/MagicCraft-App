package com.jose.magiccraftapp.client

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ClientAdapterViewPagerDeck(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ClientDeckManageSeeCardsFragment()
            1 -> ClientDeckManageAddCardsFragment()
            2 -> ClientDeckManageStatisticsFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}