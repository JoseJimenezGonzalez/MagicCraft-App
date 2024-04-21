package com.jose.magiccraftapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.databinding.FragmentClientLifeBinding
import com.jose.magiccraftapp.databinding.FragmentClientStormBinding

class ClientStormFragment: Fragment() {

    private var _binding: FragmentClientStormBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientStormBinding.inflate(inflater, container, false)
        return binding.root
    }

}