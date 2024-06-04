package com.jose.magiccraftapp.ui.activity

import android.app.AlertDialog
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.TextView
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

        checkNotificationPermission()
    }

    private fun checkNotificationPermission() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (!notificationManager.areNotificationsEnabled()) {
            // Las notificaciones no están habilitadas. Muestra un diálogo al usuario.
            val dialogView = layoutInflater.inflate(R.layout.dialog_custom, null)

            val tvTitle = dialogView.findViewById<TextView>(R.id.tvTitle)
            val tvMessage = dialogView.findViewById<TextView>(R.id.tvMessage)
            val btnYes = dialogView.findViewById<Button>(R.id.btnYes)
            val btnNo = dialogView.findViewById<Button>(R.id.btnNo)

            tvTitle.text = "Permisos de notificación"
            tvMessage.text = "Las notificaciones están deshabilitadas. ¿Deseas habilitarlas?"

            val alertDialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .create()

            btnYes.setOnClickListener {
                // El usuario ha aceptado. Abre la configuración de la aplicación para que el usuario pueda habilitar las notificaciones.
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                    putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                }
                startActivity(intent)
                alertDialog.dismiss()
            }

            btnNo.setOnClickListener {
                // El usuario ha rechazado. Cierra el diálogo.
                alertDialog.dismiss()
            }

            alertDialog.show()
        }
    }

    private fun isValidPassword(password: String): Boolean = password.isNotBlank() && password.length > 7
    private fun isValidMail(mail: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(mail).matches()

    private fun paintErrorMail(isMailValid: Boolean){
        if(isMailValid){
            binding.tilCorreo.error = null
        }else{
            binding.tilCorreo.error = "Vacío o no corresponde con formato correo"
        }
    }

    private fun paintErrorPassword(isPasswordValid: Boolean){
        if(isPasswordValid){
            binding.tilPassword.error = null
        }else{
            binding.tilPassword.error = "No puede estar vacío y tiene que tener más de 7 caracteres"
        }
    }

    private fun actionButtonLogin() {
        binding.btnIniciarSesion.setOnClickListener {
            //Comprobar que los datos que introduce son validos
            val mail = binding.tietCorreo.text.toString().trim()
            val password = binding.tietPassword.text.toString().trim()
            val isMailCorrect = isValidMail(mail)
            val isPasswordCorrect = isValidPassword(password)
            paintErrorMail(isMailCorrect)
            paintErrorPassword(isPasswordCorrect)
            //Si los datos tienen forma correcta comprobamos si existe en auth
            if(isMailCorrect && isPasswordCorrect){
                binding.progressBar.visibility = View.VISIBLE

                userViewModel.obtainUser(mail, password).observe(this, Observer {  usuario ->
                    if(usuario == null){
                        generateToast("Usuario incorrecto")
                    }else{
                        generateToast("Usuario correcto")
                        //Guardar usuario en componion
                        CurrentUser.currentUser = usuario
                        //Guardar datos de usuario en la shared
                        val userName = usuario.userName
                        val realName = usuario.realName
                        val idUsuaio = usuario.id
                        val mail = usuario.mail
                        val typeUser = usuario.typeUser
                        val password = usuario.password
                        val urlImageFirebase = usuario.urlImageFirebase
                        val login = "login"
                        val modoDia = true
                        val botonDia = true
                        saveSharedPreferences(userName, realName, idUsuaio, mail, typeUser, password, urlImageFirebase, login, modoDia, botonDia)
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
    }

    private fun saveSharedPreferences(
        userName: String,
        realName: String,
        idUsuaio: String,
        mail: String,
        typeUser: String,
        password: String,
        urlImageFirebase: String,
        login: String,
        modoDia: Boolean,
        botonDia: Boolean
    ) {
        this.putPreference("userName", userName)
        this.putPreference("realName", realName)
        this.putPreference("idUsuario", idUsuaio)
        this.putPreference("mail", mail)
        this.putPreference("typeUser", typeUser)
        this.putPreference("password", password)
        this.putPreference("urlImageFirebase", urlImageFirebase)
        this.putPreference("login", login)
        this.putPreference("modo_dia", modoDia)
        this.putPreference("boton_dia", botonDia)
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