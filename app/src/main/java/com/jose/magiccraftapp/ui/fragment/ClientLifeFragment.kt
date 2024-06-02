package com.jose.magiccraftapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jose.magiccraftapp.databinding.FragmentClientLifeBinding
import java.util.concurrent.Semaphore


class ClientLifeFragment : Fragment() {

    private var _binding: FragmentClientLifeBinding? = null

    private val binding get() = _binding!!

    var lifeYou = 20

    var lifeOther = 20

    private val semaphore = Semaphore(1)
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
            semaphore.acquire()
            lifeOther--
            binding.tvLifeOther.text = lifeOther.toString()
            semaphore.release()
        }
        //Se le da a mas
        binding.ivPlusOther.setOnClickListener {
            semaphore.acquire()
            lifeOther++
            binding.tvLifeOther.text = lifeOther.toString()
            semaphore.release()
        }
        binding.ivAutorenewOther.setOnClickListener {
            semaphore.acquire()
            lifeOther = 20
            binding.tvLifeOther.text = lifeOther.toString()
            semaphore.release()
        }
    }

    private fun setUpCounterLifeYou() {
        binding.tvLifeYou.text = lifeYou.toString()
        //Se le da a menos
        binding.ivMinusYou.setOnClickListener {
            semaphore.acquire()
            lifeYou--
            binding.tvLifeYou.text = lifeYou.toString()
            semaphore.release()
        }
        //Se le da a mas
        binding.ivPlusYou.setOnClickListener {
            semaphore.acquire()
            lifeYou++
            binding.tvLifeYou.text = lifeYou.toString()
            semaphore.release()
        }
        binding.ivAutorenewYou.setOnClickListener {
            semaphore.acquire()
            lifeYou = 20
            binding.tvLifeYou.text = lifeYou.toString()
            semaphore.release()
        }
    }

}