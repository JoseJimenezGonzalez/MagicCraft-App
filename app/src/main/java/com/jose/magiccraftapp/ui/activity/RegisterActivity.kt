package com.jose.magiccraftapp.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.data.model.User
import com.jose.magiccraftapp.data.model.UserForm
import com.jose.magiccraftapp.data.viewmodel.UsuarioViewModel
import com.jose.magiccraftapp.databinding.ActivityRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var binding: ActivityRegisterBinding

    private var urlImagen: Uri? = null

    private lateinit var cover: ImageView

    private var userName = ""

    @Inject
    lateinit var dbRef: DatabaseReference

    @Inject
    lateinit var stoRef: StorageReference

    lateinit var job: Job

    @Inject
    lateinit var auth: FirebaseAuth

    private val userViewModel: UsuarioViewModel by viewModels()

    private var listOfUserNames: MutableList<String> = mutableListOf()

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

        cover = binding.ivAccount

        job = Job()

        //Usuario acciona el boton de registrarse

        actionButtonRegister()

        actionButtonGoToLogin()

        setUpButtonImageViewGalery()

        obtainListUsernames()

    }

    private fun obtainListUsernames() {
        userViewModel.getUsernames().observe(this, Observer { listOfUsernames ->
            listOfUserNames = listOfUsernames
        })
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
            userName = binding.tietNombre.text.toString().trim()
            val realName = binding.tietNombreReal.text.toString().trim()
            val mail = binding.tietCorreo.text.toString().trim()
            val password = binding.tietPassword.text.toString().trim()
            val repeatPassword = binding.tietRepetirPassword.text.toString().trim()

            //Variables de los chivatos
            val isUserNameValid = isUserNameValid(userName)
            val isRealnameValid = isRealNameValid(realName)
            val isMailValid = isValidMail(mail)
            val isPasswordValid = isValidPassword(password)
            val isRepeatPasswordValid = isValidPassword(repeatPassword)
            val arePasswordEquals = areValidPasswordsEquals(password, repeatPassword)
            val isImageValid = isValidImage()

            //Creo un objeto UserForm (persona fromulario que recoge los datos anteriores)
            val userForm = UserForm(userName, realName, mail, password)
            //Llamada a los metodos de pintar errores
            paintErrorUserName(isUserNameValid)
            paintErrorRealName(isRealnameValid)
            paintErrorMail(isMailValid)
            paintErrorPassword(isPasswordValid)
            paintErrorRepeatPassword(isRepeatPasswordValid)
            if(isPasswordValid && isRepeatPasswordValid){
                paintErrorEqualsPasswords(isPasswordValid, isRepeatPasswordValid, arePasswordEquals)
            }

            //Valida si esta ok para llamar a otras funciones
            validateAll(isUserNameValid, isRealnameValid, isMailValid, isPasswordValid, isRepeatPasswordValid, arePasswordEquals, userForm, isImageValid)
        }

    }

    private fun validateAll(isNameValid: Boolean, isSurnameValid: Boolean, isMailValid: Boolean, isPasswordValid: Boolean, isRepeatPasswordValid: Boolean, arePasswordEquals: Boolean, userForm: UserForm, isImageValid: Boolean) {
        if(isNameValid && isSurnameValid && isMailValid && isPasswordValid && isRepeatPasswordValid && arePasswordEquals && isImageValid){
            //Hacerlo en view model
            //Comprobamos si existe el nombre de usuario previamente
            if(!listOfUserNames.contains(userName.lowercase())){
                registerUserAuth(userForm)
            }else{
                generateToast("Ya existe un usuario con ese nombre de usuario")
            }
        }else{
            Toast.makeText(this, "Hay campos erróneos", Toast.LENGTH_SHORT).show()
        }
    }



    //Validar los datos
    private fun isUserNameValid(name: String): Boolean = name.isNotBlank()
    private fun isRealNameValid(surname: String): Boolean = surname.isNotBlank()
    private fun isValidMail(mail: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(mail).matches()
    private fun isValidPassword(password: String): Boolean = password.isNotBlank() && password.length > 7
    private fun areValidPasswordsEquals(password: String, repeatPassword: String) = password == repeatPassword

    //Indicar error en los edit text
    private fun paintErrorUserName(isNameValid: Boolean){
        if(isNameValid){
            binding.tilNombre.error = null
        }else{
            binding.tilNombre.error = "El nombre de usuario no puede estar vacío"
        }
    }

    private fun paintErrorRealName(isSurnameValid: Boolean){
        if(isSurnameValid){
            binding.tilNombreReal.error = null
        }else{
            binding.tilNombreReal.error = "Nombre y apellidos no puede estar vacío"
        }
    }

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

    private fun paintErrorRepeatPassword(isRepeatPasswordValid: Boolean){
        if(isRepeatPasswordValid){
            binding.tilRepetirPassword.error = null
        }else{
            binding.tilRepetirPassword.error = "No puede estar vacío y tiene que tener más de 7 caracteres"
        }
    }

    private fun isValidImage(): Boolean{
        if(urlImagen == null){
            generateToast("Tienes que seleccionar una imagen")
            return false
        }else{
            return true
        }
    }

    private fun paintErrorEqualsPasswords(isPasswordValid: Boolean, isRepeatPasswordValid: Boolean, arePasswordsEquals: Boolean){
        if(isPasswordValid && isRepeatPasswordValid && arePasswordsEquals){
            binding.tilPassword.error = null
            binding.tilRepetirPassword.error = null
        }else{
            binding.tilPassword.error = "No coinciden las contraseñas"
            binding.tilRepetirPassword.error = "No coinciden las contraseñas"
        }
    }

    suspend fun saveImageCover(stoRef: StorageReference, idUser: String, imagen: Uri):String{

        val urlCoverFirebase: Uri = stoRef.child("MagicCraft").child("Image_Cover_User").child(idUser)
            .putFile(imagen).await().storage.downloadUrl.await()

        return urlCoverFirebase.toString()
    }

    private fun generateToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setUpButtonImageViewGalery() {
        //Cuando le da click en la imagen para guardar imagen del juego
        binding.ivAccount.setOnClickListener {
            galeryAccess.launch("image/*")
        }
    }

    fun registerUserAuth(userForm: UserForm) {
        val mail = userForm.mail
        val password = userForm.password
        auth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                val userAuth = auth.currentUser
                //Lamada a funcion para meter los datos en la RealTimeDatabase
                registerUserRealTimeDatabase(userForm, userAuth!!)
            }else{
                generateToast("Ya existe una cuenta con ese correo en nuestra base de datos")
            }
        }
    }

    private fun registerUserRealTimeDatabase(userForm: UserForm, userAuth: FirebaseUser) {
        launch {
            val id = userAuth.uid
            val isUserAdmin = isAdministratorUser(userForm.mail)
            val typeUser = typeOfUser(isUserAdmin)
            val urlImageFirebase = saveImageCover(stoRef, id, urlImagen!!)
            dbRef.child("MagicCraft").child("Users").child(id).setValue(
                User(
                    id,
                    userForm.name,
                    userForm.surname,
                    userForm.mail,
                    userForm.password,
                    typeUser,
                    urlImageFirebase
                )
            )
        }
        generateToast("Se ha registrado correctamente")
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun isAdministratorUser(email: String): Boolean = email=="administrador@gmail.com"

    private fun typeOfUser(bol: Boolean): String = if (bol) "administrador" else "cliente"

    private val galeryAccess =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                urlImagen = uri
                cover.setImageURI(uri)
            }
        }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

}