package com.jose.magiccraftapp.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.databinding.FragmentClientHomeBinding

class ClientHomeFragment : Fragment() {

    private var _binding: FragmentClientHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Codigo
        setUpButtonOpenUrlRules()
        setUpButtonOpenSetting()
    }

    private fun setUpButtonOpenSetting() {
        binding.ivSettings.setOnClickListener {
            findNavController().navigate(R.id.action_clientHomeFragment_to_clientSettingFragment)
        }
    }

    private fun setUpButtonOpenUrlRules() {
        binding.cvRules.setOnClickListener {
            val url = "https://media.wizards.com/2024/downloads/MagicCompRules%2004102024.pdf"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }



}