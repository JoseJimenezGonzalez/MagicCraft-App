package com.jose.magiccraftapp.ui.activity

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.data.model.CurrentUser
import com.jose.magiccraftapp.data.model.User
import com.jose.magiccraftapp.databinding.ActivitySplashBinding
import com.jose.magiccraftapp.util.getStringPreference

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onResume() {
        super.onResume()

        //Codigo
        binding.animation.setAnimation(R.raw.animation_dodecahedron)
        binding.animation.playAnimation()
        binding.animation.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                // Código para ejecutar cuando la animación comienza
            }

            override fun onAnimationEnd(animation: Animator) {
                // Código para ejecutar cuando la animación termina
                //Si tiene datos en la shared nos saltamos el login
                val name = getStringPreference("name")
                val surname = getStringPreference("surname")
                val idUsuario = getStringPreference("idUsuario")
                val mail = getStringPreference("mail")
                val typeUser = getStringPreference("typeUser")
                val password = getStringPreference("password")
                val urlImageFirebase = getStringPreference("urlImageFirebase")
                val login = getStringPreference("login")
                if (name.isNotBlank() && surname.isNotBlank() && idUsuario.isNotBlank() && mail.isNotBlank() && typeUser.isNotBlank() && password.isNotBlank() && login.isNotBlank() && urlImageFirebase.isNotBlank()) {
                    if(login == "login"){
                        //Está guardado el usuario, nos saltamos el login
                        //Iniciamos el companion del usuario
                        CurrentUser.currentUser = User(
                            idUsuario,
                            name,
                            surname,
                            mail,
                            password,
                            typeUser,
                            urlImageFirebase)
                        if(typeUser == "administrador"){
                            val intent = Intent(this@SplashActivity, MainAdminActivity::class.java)
                            startActivity(intent)
                        }else{
                            val intent = Intent(this@SplashActivity, MainClientActivity::class.java)
                            startActivity(intent)
                        }
                    }
                } else {
                    // Ir a login
                    val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                    startActivity(intent)
                }

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