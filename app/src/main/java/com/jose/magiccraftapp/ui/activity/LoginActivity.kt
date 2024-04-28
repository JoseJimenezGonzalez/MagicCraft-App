package com.jose.magiccraftapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.data.model.CurrentUser
import com.jose.magiccraftapp.data.viewmodel.UsuarioViewModel
import com.jose.magiccraftapp.databinding.ActivityLoginBinding
import com.jose.magiccraftapp.util.putPreference
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
    }

    private fun actionButtonLogin() {
        binding.btnIniciarSesion.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val mail = binding.tietCorreo.text.toString().trim()
            val password = binding.tietPassword.text.toString().trim()
            userViewModel.obtainUser(mail, password).observe(this, Observer {  usuario ->
                if(usuario == null){
                    generateToast("Usuario incorrecto")
                }else{
                    generateToast("Usuario correcto")
                    //Guardar usuario en componion
                    CurrentUser.currentUser = usuario
                    //Guardar datos de usuario en la shared
                    val name = usuario.name
                    val surname = usuario.surname
                    val idUsuaio = usuario.id
                    val mail = usuario.mail
                    val typeUser = usuario.typeUser
                    val password = usuario.password
                    val urlImageFirebase = usuario.urlImageFirebase
                    val login = "login"
                    saveSharedPreferences(name, surname, idUsuaio, mail, typeUser, password, urlImageFirebase, login)
                    if(mail == "administrador@gmail.com"){
                        val intent = Intent(this, MainAdminActivity::class.java)
                        startActivity(intent)
                    }else{
                        val intent = Intent(this, MainClientActivity::class.java)
                        startActivity(intent)
                    }
                }
            })
        }
    }

    private fun saveSharedPreferences(
        name: String,
        surname: String,
        idUsuaio: String,
        mail: String,
        typeUser: String,
        password: String,
        urlImageFirebase: String,
        login: String
    ) {
        this.putPreference("name", name)
        this.putPreference("surname", surname)
        this.putPreference("idUsuario", idUsuaio)
        this.putPreference("mail", mail)
        this.putPreference("typeUser", typeUser)
        this.putPreference("password", password)
        this.putPreference("urlImageFirebase", urlImageFirebase)
        this.putPreference("login", login)
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