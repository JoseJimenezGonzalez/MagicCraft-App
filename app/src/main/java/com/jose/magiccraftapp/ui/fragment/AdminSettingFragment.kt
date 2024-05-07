package com.jose.magiccraftapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.databinding.FragmentAdminSettingBinding
import com.jose.magiccraftapp.ui.activity.MainAdminActivity
import com.jose.magiccraftapp.ui.activity.MainClientActivity

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
    }

    private fun setUpRadioButtons() {
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rbClaro -> {
                    // Cambiar al tema claro
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    ActivityCompat.recreate(MainAdminActivity()) // Reconstruye la actividad para aplicar el nuevo tema
                }
                R.id.rbOscuro -> {
                    // Cambiar al tema oscuro
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    ActivityCompat.recreate(MainAdminActivity()) // Reconstruye la actividad para aplicar el nuevo tema
                }
            }
        }
    }

}