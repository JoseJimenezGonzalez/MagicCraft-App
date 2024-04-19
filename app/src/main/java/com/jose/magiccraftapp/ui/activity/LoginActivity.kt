package com.jose.magiccraftapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.databinding.ActivityLoginBinding
import com.jose.magiccraftapp.data.viewmodel.LoginActivityViewModel
import com.jose.magiccraftapp.data.viewmodel.UsuarioViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    @Inject
    lateinit var dbRef: DatabaseReference

    @Inject
    lateinit var auth: FirebaseAuth

    private val userViewModel: UsuarioViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Codigo

        actionButtonGoToRegister()

        actionButtonLogin()

        //Observadores
        userViewModel.toastMessage.observe(this){ message ->
            generateToast(message)
        }
        userViewModel.routeToNavigate.observe(this) { activityClass ->
            if (activityClass != null) {
                goToMainActivity(activityClass)
            }
        }
    }

    private fun goToMainActivity(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
    }

    private fun actionButtonLogin() {
        binding.btnIniciarSesion.setOnClickListener {
            val mail = binding.tietCorreo.text.toString().trim()
            val password = binding.tietPassword.text.toString().trim()
            userViewModel.loginAuth(mail, password)
        }
    }

    private fun actionButtonGoToRegister() {
        binding.tvCrearCuenta.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun generateToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}