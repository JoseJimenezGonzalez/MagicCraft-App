package com.jose.magiccraftapp.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jose.magiccraftapp.ui.fragment.ClientDiceFragment
import com.jose.magiccraftapp.ui.fragment.ClientLifeFragment
import com.jose.magiccraftapp.ui.fragment.ClientStormFragment

class ClientAdapterViewPagerTools (fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ClientLifeFragment()
            1 -> ClientStormFragment()
            2 -> ClientDiceFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}