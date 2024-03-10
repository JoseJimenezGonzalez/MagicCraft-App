package com.jose.magiccraftapp.client

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.databinding.FragmentAdminDeckBinding
import com.jose.magiccraftapp.databinding.FragmentClientChatBinding

class ClientChatFragment : Fragment() {

    private var _binding: FragmentClientChatBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientChatBinding.inflate(inflater, container, false)
        return binding.root
    }

}