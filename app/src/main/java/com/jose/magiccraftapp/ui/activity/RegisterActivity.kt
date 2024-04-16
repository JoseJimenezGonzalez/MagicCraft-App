package com.jose.magiccraftapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.databinding.ActivityRegisterBinding
import com.jose.magiccraftapp.datasource.model.UserForm
import com.jose.magiccraftapp.datasource.viewmodel.RegisterActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    @Inject
    lateinit var dbRef: DatabaseReference

    @Inject
    lateinit var auth: FirebaseAuth

    private val viewModel: RegisterActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Codigo

        //Usuario acciona el boton de registrarse

        actionButtonRegister()

        actionButtonGoToLogin()

        //Observo variable toast del view model
        viewModel.messageToast.observe(this) { message ->
            generateToast(message)
        }
    }

    private fun actionButtonGoToLogin() {
        binding.tvVolverIniciarSesion.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun actionButtonRegister() {
        binding.btnRegistrarUsuario.setOnClickListener {

            //Primero nos traemos todos los datos del usuario
            val name = binding.tietNombre.text.toString().trim()
            val surname = binding.tietApellidos.text.toString().trim()
            val mail = binding.tietCorreo.text.toString().trim()
            val password = binding.tietPassword.text.toString().trim()
            val repeatPassword = binding.tietRepetirPassword.text.toString().trim()

            //Variables de los chivatos
            val isNameValid = isValidName(name)
            val isSurnameValid = isValidSurname(surname)
            val isMailValid = isValidMail(mail)
            val isPasswordValid = isValidPassword(password)
            val isRepeatPasswordValid = isValidPassword(repeatPassword)
            val arePasswordEquals = areValidPasswordsEquals(password, repeatPassword)

            //Creo un objeto UserForm (persona fromulario que recoge los datos anteriores)
            val userForm = UserForm(name, surname, mail, password)
            //Llamada a los metodos de pintar errores
            paintErrorName(isNameValid)
            paintErrorSurname(isSurnameValid)
            paintErrorMail(isMailValid)
            paintErrorPassword(isPasswordValid)
            paintErrorRepeatPassword(isRepeatPasswordValid)
            paintErrorEqualsPasswords(isPasswordValid, isRepeatPasswordValid, arePasswordEquals)

            //Valida si esta ok para llamar a otras funciones
            validateAll(isNameValid, isSurnameValid, isMailValid, isPasswordValid, isRepeatPasswordValid, arePasswordEquals, userForm)
        }

    }

    private fun validateAll(isNameValid: Boolean, isSurnameValid: Boolean, isMailValid: Boolean, isPasswordValid: Boolean, isRepeatPasswordValid: Boolean, arePasswordEquals: Boolean, userForm: UserForm) {
        if(isNameValid && isSurnameValid && isMailValid && isPasswordValid && isRepeatPasswordValid && arePasswordEquals){
            viewModel.registerUserAuth(userForm)
        }else{
            Toast.makeText(this, "Hay campos erróneos", Toast.LENGTH_SHORT).show()
        }
    }



    //Validar los datos
    private fun isValidName(name: String): Boolean = name.isNotBlank()
    private fun isValidSurname(surname: String): Boolean = surname.isNotBlank()
    private fun isValidMail(mail: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(mail).matches()
    private fun isValidPassword(password: String): Boolean = password.isNotBlank() && password.length > 7
    private fun areValidPasswordsEquals(password: String, repeatPassword: String) = password == repeatPassword

    //Indicar error en los edit text
    private fun paintErrorName(isNameValid: Boolean){
        if(isNameValid){
            binding.tietNombre.error = null
        }else{
            binding.tietNombre.error = "No puede estar vacío"
        }
    }

    private fun paintErrorSurname(isSurnameValid: Boolean){
        if(isSurnameValid){
            binding.tietApellidos.error = null
        }else{
            binding.tietApellidos.error = "No puede estar vacío"
        }
    }

    private fun paintErrorMail(isMailValid: Boolean){
        if(isMailValid){
            binding.tietCorreo.error = null
        }else{
            binding.tietCorreo.error = "Vacío o no corresponde con formato correo"
        }
    }

    private fun paintErrorPassword(isPasswordValid: Boolean){
        if(isPasswordValid){
            binding.tietPassword.error = null
        }else{
            binding.tietPassword.error = "No puede estar vacío y tiene que tener más de 7 caracteres"
        }
    }

    private fun paintErrorRepeatPassword(isRepeatPasswordValid: Boolean){
        if(isRepeatPasswordValid){
            binding.tietRepetirPassword.error = null
        }else{
            binding.tietRepetirPassword.error = "No puede estar vacío y tiene que tener más de 7 caracteres"
        }
    }

    private fun paintErrorEqualsPasswords(isPasswordValid: Boolean, isRepeatPasswordValid: Boolean, arePasswordsEquals: Boolean){
        if(isPasswordValid && isRepeatPasswordValid && arePasswordsEquals){
            binding.tietPassword.error = null
            binding.tietRepetirPassword.error = null
        }else{
            binding.tietPassword.error = "No coinciden las contraseñas"
            binding.tietRepetirPassword.error = "No coinciden las contraseñas"
        }
    }

    private fun generateToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}