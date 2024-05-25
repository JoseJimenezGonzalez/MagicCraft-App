package com.jose.magiccraftapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.databinding.FragmentAdminSettingBinding
import com.jose.magiccraftapp.ui.activity.LoginActivity
import com.jose.magiccraftapp.ui.adapter.ModalBottomSheetAbout
import com.jose.magiccraftapp.ui.adapter.ModalBottomSheetPolicy
import com.jose.magiccraftapp.util.putPreference

class AdminSettingFragment : Fragment() {

    private var _binding: FragmentAdminSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRadioButtons()
        setUpButtonLogOut()
        setUpButtonsLinearLayout()
    }

    private fun setUpButtonLogOut() {
        binding.llLogOut.setOnClickListener {
            requireContext().putPreference("login", "")
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setUpRadioButtons() {
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val activity = activity
            if (activity is AppCompatActivity) {
                when (checkedId) {
                    R.id.rbClaro -> {
                        requireContext().putPreference("modo_dia", true)
                        requireContext().putPreference("boton_dia", true)
                    }
                    R.id.rbOscuro -> {
                        requireContext().putPreference("modo_dia", false)
                        requireContext().putPreference("boton_dia", false)
                    }
                }
            }
        }
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

}