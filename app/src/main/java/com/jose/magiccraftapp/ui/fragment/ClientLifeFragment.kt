package com.jose.magiccraftapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jose.magiccraftapp.databinding.FragmentClientLifeBinding


class ClientLifeFragment : Fragment() {

    private var _binding: FragmentClientLifeBinding? = null

    private val binding get() = _binding!!

    var lifeYou = 20

    var lifeOther = 20

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientLifeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Codigo
        setUpCounterLifeYou()
        setUpCounterLifeOther()
    }

    private fun setUpCounterLifeOther() {
        binding.tvLifeOther.text = lifeOther.toString()
        //Se le da a menos
        binding.ivMinusOther.setOnClickListener {
            lifeOther--
            binding.tvLifeOther.text = lifeOther.toString()
        }
        //Se le da a mas
        binding.ivPlusOther.setOnClickListener {
            lifeOther++
            binding.tvLifeOther.text = lifeOther.toString()
        }
        binding.ivAutorenewOther.setOnClickListener {
            lifeOther = 20
            binding.tvLifeOther.text = lifeOther.toString()
        }
    }

    private fun setUpCounterLifeYou() {
        binding.tvLifeYou.text = lifeYou.toString()
        //Se le da a menos
        binding.ivMinusYou.setOnClickListener {
            lifeYou--
            binding.tvLifeYou.text = lifeYou.toString()
        }
        //Se le da a mas
        binding.ivPlusYou.setOnClickListener {
            lifeYou++
            binding.tvLifeYou.text = lifeYou.toString()
        }
        binding.ivAutorenewYou.setOnClickListener {
            lifeYou = 20
            binding.tvLifeYou.text = lifeYou.toString()
        }
    }

}