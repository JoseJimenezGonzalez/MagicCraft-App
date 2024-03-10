package com.jose.magiccraftapp.client

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.databinding.FragmentAdminDeckBinding
import com.jose.magiccraftapp.databinding.FragmentClientToolsBinding

class ClientToolsFragment : Fragment() {

    private var _binding: FragmentClientToolsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientToolsBinding.inflate(inflater, container, false)
        return binding.root
    }

}