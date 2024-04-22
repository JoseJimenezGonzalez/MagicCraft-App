package com.jose.magiccraftapp.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jose.magiccraftapp.ui.fragment.AdminNewsManageAddFragment
import com.jose.magiccraftapp.ui.fragment.AdminNewsManageListFragment

class AdminAdapterViewPagerNews (fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AdminNewsManageAddFragment()
            1 -> AdminNewsManageListFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}