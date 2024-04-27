package com.jose.magiccraftapp.ui.fragment

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.databinding.FragmentClientDiceBinding


class ClientDiceFragment: Fragment() {

    private var _binding: FragmentClientDiceBinding? = null

    private val binding get() = _binding!!

    val listPlayer = listOf(
        "Player 1 start", "Player 2 start"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientDiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.tvStart.text = ""
        binding.animation.setAnimation(R.raw.animation_dice)
        binding.animation.playAnimation()
        binding.animation.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                // Código para ejecutar cuando la animación comienza
            }

            override fun onAnimationEnd(animation: Animator) {
                // Código para ejecutar cuando la animación termina
                binding.tvStart.text = listPlayer.shuffled()[0]
            }

            override fun onAnimationCancel(animation: Animator) {
                // Código para ejecutar cuando la animación se cancela
            }

            override fun onAnimationRepeat(animation: Animator) {
                // Código para ejecutar cuando la animación se repite
            }
        })
    }

}