package com.jose.magiccraftapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.databinding.FragmentAdminChatBinding
import com.jose.magiccraftapp.databinding.FragmentAdminDeckBinding

class AdminDeckFragment : Fragment() {

    private var _binding: FragmentAdminDeckBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminDeckBinding.inflate(inflater, container, false)
        return binding.root
    }

}