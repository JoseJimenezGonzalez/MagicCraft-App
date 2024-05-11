package com.jose.magiccraftapp.ui.fragment

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat.recreate
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.databinding.FragmentClientHomeBinding
import com.jose.magiccraftapp.databinding.FragmentClientSettingBinding
import com.jose.magiccraftapp.ui.activity.LoginActivity
import com.jose.magiccraftapp.ui.activity.MainClientActivity
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