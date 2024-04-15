package com.jose.magiccraftapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.databinding.FragmentAdminDeckBinding
import com.jose.magiccraftapp.databinding.FragmentAdminSettingBinding

class AdminSettingFragment : Fragment() {

    private var _binding: FragmentAdminSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

}