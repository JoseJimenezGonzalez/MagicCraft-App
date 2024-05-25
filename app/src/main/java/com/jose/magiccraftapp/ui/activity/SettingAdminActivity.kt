package com.jose.magiccraftapp.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.databinding.ActivitySettingAdminBinding
import com.jose.magiccraftapp.ui.adapter.ModalBottomSheetAbout
import com.jose.magiccraftapp.ui.adapter.ModalBottomSheetPolicy
import com.jose.magiccraftapp.util.getBooleanPreference
import com.jose.magiccraftapp.util.putPreference

class SettingAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySettingAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setUpButtonLogOut()
        setUpButtonsLinearLayout()
        setUpRadioButtons()
        setUpButtonToMain()
    }

    private fun setUpButtonToMain() {
        binding.ivBack.setOnClickListener {
            val intent = Intent(this, MainAdminActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setUpButtonLogOut() {
        binding.llLogOut.setOnClickListener {
            this.putPreference("login", "")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setUpRadioButtons() {
        // Obtén el valor de la preferencia compartida
        val botonDia = this.getBooleanPreference("boton_dia")

        // Establece el botón de radio seleccionado por defecto
        if (botonDia) {
            binding.rbClaro.isChecked = true
        } else {
            binding.rbOscuro.isChecked = true
        }

        // Configura el comportamiento cuando se selecciona un botón de radio
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rbClaro -> {
                    this.putPreference("modo_dia", true)
                    this.putPreference("boton_dia", true)
                }
                R.id.rbOscuro -> {
                    this.putPreference("modo_dia", false)
                    this.putPreference("boton_dia", false)
                }
            }
        }
    }

    private fun setUpButtonsLinearLayout() {
        binding.llAbout.setOnClickListener {
            val modal = ModalBottomSheetAbout()
            modal.show(supportFragmentManager, ModalBottomSheetAbout::class.java.simpleName)
        }
        binding.llPolicy.setOnClickListener {
            val modal = ModalBottomSheetPolicy()
            modal.show(supportFragmentManager, ModalBottomSheetPolicy::class.java.simpleName)
        }
    }
}