package com.jose.magiccraftapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.databinding.FragmentClientSettingBinding
import com.jose.magiccraftapp.ui.activity.LoginActivity
import com.jose.magiccraftapp.ui.adapter.ModalBottomSheetAbout
import com.jose.magiccraftapp.ui.adapter.ModalBottomSheetPolicy
import com.jose.magiccraftapp.util.putPreference

class ClientSettingFragment : Fragment() {

    private var _binding: FragmentClientSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRadioButtons()
        setUpButtonLogOut()
        setUpButtonsLinearLayout()
    }

    private fun setUpButtonsLinearLayout() {
        binding.llAbout.setOnClickListener {
            val modal = ModalBottomSheetAbout()
            modal.show(childFragmentManager, ModalBottomSheetAbout::class.java.simpleName)
        }
        binding.llPolicy.setOnClickListener {
            val modal = ModalBottomSheetPolicy()
            modal.show(childFragmentManager, ModalBottomSheetPolicy::class.java.simpleName)
        }
    }

    private fun setUpRadioButtons() {
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rbClaro -> {
                    // Cambiar al tema claro
                    requireContext().putPreference("theme", "light")
                }
                R.id.rbOscuro -> {
                    // Cambiar al tema oscuro
                    requireContext().putPreference("theme", "dark")
                }
            }
        }
    }

    private fun setUpButtonLogOut() {
        binding.llLogOut.setOnClickListener {
            requireContext().putPreference("login", "")
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
    }


}