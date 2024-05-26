package com.jose.magiccraftapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jose.magiccraftapp.databinding.FragmentClientStormBinding

class ClientStormFragment: Fragment() {

    private var _binding: FragmentClientStormBinding? = null

    private val binding get() = _binding!!

    private var manaBlue = 0
    private var manaRed = 0
    private var manaBlack = 0
    private var manaWhite = 0
    private var manaGreen = 0
    private var storm = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientStormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpButtons()
    }

    private fun setUpButtons() {
        binding.ivMinusBlue.setOnClickListener {
            manaBlue -= 1
            binding.tvBlue.text = manaBlue.toString()
        }
        binding.ivPlusBlue.setOnClickListener {
            manaBlue += 1
            binding.tvBlue.text = manaBlue.toString()
        }

        binding.ivMinusBlack.setOnClickListener {
            manaBlack -= 1
            binding.tvBlack.text = manaBlack.toString()
        }
        binding.ivPlusBlack.setOnClickListener {
            manaBlack += 1
            binding.tvBlack.text = manaBlack.toString()
        }

        binding.ivMinusRed.setOnClickListener {
            manaRed -= 1
            binding.tvRed.text = manaRed.toString()
        }
        binding.ivPlusRed.setOnClickListener {
            manaRed += 1
            binding.tvRed.text = manaRed.toString()
        }

        binding.ivMinusGreen.setOnClickListener {
            manaGreen -= 1
            binding.tvGreen.text = manaGreen.toString()
        }
        binding.ivPlusGreen.setOnClickListener {
            manaGreen += 1
            binding.tvGreen.text = manaGreen.toString()
        }

        binding.ivMinusWhite.setOnClickListener {
            manaWhite -= 1
            binding.tvWhite.text = manaWhite.toString()
        }
        binding.ivPlusWhite.setOnClickListener {
            manaWhite += 1
            binding.tvWhite.text = manaWhite.toString()
        }

        binding.ivMinusTorment.setOnClickListener {
            storm -= 1
            binding.tvTorment.text = storm.toString()
        }
        binding.ivPlusTorment.setOnClickListener {
            storm += 1
            binding.tvTorment.text = storm.toString()
        }
    }

}