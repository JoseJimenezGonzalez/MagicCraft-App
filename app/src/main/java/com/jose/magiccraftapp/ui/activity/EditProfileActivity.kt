package com.jose.magiccraftapp.ui.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.data.model.CurrentUser
import com.jose.magiccraftapp.data.model.User
import com.jose.magiccraftapp.databinding.ActivityEditProfileBinding
import com.jose.magiccraftapp.util.putPreference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class EditProfileActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var binding: ActivityEditProfileBinding

    private var urlImagen: Uri? = null

    private lateinit var cover: ImageView

    @Inject
    lateinit var dbRef: DatabaseReference

    @Inject
    lateinit var stoRef: StorageReference

    lateinit var job: Job

    private var urlImFirebase = ""

    private var buttonGalery = false

    private lateinit var auth: FirebaseAuth

    var nombreUsuario = ""

    var nombreReal = ""

    var password = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Codigo

        cover = binding.ivAccount

        job = Job()

        setUpAncientData()

        setUpButtonImageViewGalery()

        setUpButtonImageViewBack()

        setUpButtonEditProfile()

    }

    private fun setUpButtonEditProfile() {
        binding.btnRegistrarUsuario.setOnClickListener {
            //Nos traemos los datos de entrada
            nombreUsuario = binding.tietNombre.text.toString()
            nombreReal = binding.tietApellidos.text.toString()
            password = binding.tietPassword.text.toString()
            //Obtenemos los chivatos
            val isNombreUsuarioValid = isValidName(nombreUsuario)
            val isNombreRealValid = isValidName(nombreReal)
            val isPasswordValid = isValidPassword(password)
            var isImageValid = true
            if(buttonGalery){
                isImageValid = isValidImage()
            }
            //Pintamos si tiene error
            paintErrorNombreUsuario(isNombreUsuarioValid)
            paintErrorNombreReal(isNombreRealValid)
            paintErrorPassword(isPasswordValid)
            //Validar all campos
            validateAll(isNombreUsuarioValid, isNombreRealValid, isPasswordValid, isImageValid)
        }
    }

    private fun validateAll(nombreUsuarioValid: Boolean, nombreRealValid: Boolean, passwordValid: Boolean, imageValid: Boolean) {
        if(nombreUsuarioValid && nombreRealValid && passwordValid && imageValid){
            var urlImageFirebase = CurrentUser.currentUser!!.urlImageFirebase
            launch {
                if (buttonGalery){
                    urlImageFirebase = saveImageCover(stoRef, CurrentUser.currentUser!!.id, urlImagen!!)

                }
            }
            val usuarioActualizado = User(
                CurrentUser.currentUser!!.id,
                nombreUsuario,
                nombreReal,
                CurrentUser.currentUser!!.mail,
                password,
                CurrentUser.currentUser!!.typeUser,
                urlImageFirebase
            )
            //Actualizamos pass en el auth
            val ancientPass = CurrentUser.currentUser!!.password
            if(password != ancientPass){
                Log.e("Entra", "Entra")
                auth = Firebase.auth
                val user = auth.currentUser
                user.let {
                    // Cambiar contraseña
                    user!!.updatePassword(password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.e("Entra", "Contraseña actualizada correctamente")
                            } else {
                                println("Error al actualizar la contraseña.")
                            }
                        }
                }
            }
            //Actualizamos la info de la base de datos del usuario
            dbRef.child("MagicCraft").child("Users").child(CurrentUser.currentUser!!.id).setValue(usuarioActualizado)
            //Cambiamos la info en el companion object
            CurrentUser.currentUser = usuarioActualizado
            //Actualizamos datos en la shared
            this.putPreference("name", nombreUsuario)
            this.putPreference("surname", nombreReal)
            this.putPreference("password", password)
            this.putPreference("urlImageFirebase", urlImageFirebase)
            //Ok
            generateToast("Perfil actualizado correctamente")
            if(CurrentUser.currentUser!!.typeUser == "administrador"){
                val intent = Intent(this, MainAdminActivity::class.java)
                startActivity(intent)
            }else{
                val intent = Intent(this, MainClientActivity::class.java)
                startActivity(intent)
            }
        }else{
            generateToast("Hay campos erróneos")
        }
    }


    private fun paintErrorNombreUsuario(isNameValid: Boolean){
        if(isNameValid){
            binding.tietNombre.error = null
        }else{
            binding.tietNombre.error = "No puede estar vacío"
        }
    }

    private fun isValidPassword(password: String): Boolean = password.isNotBlank() && password.length > 7

    private fun paintErrorNombreReal(isNameValid: Boolean){
        if(isNameValid){
            binding.tietApellidos.error = null
        }else{
            binding.tietApellidos.error = "No puede estar vacío"
        }
    }

    private fun paintErrorPassword(isPasswordValid: Boolean){
        if(isPasswordValid){
            binding.tietPassword.error = null
        }else{
            binding.tietPassword.error = "No puede estar vacío y tiene que tener más de 7 caracteres"
        }
    }

    private fun generateToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun isValidImage(): Boolean{
        if(urlImagen == null){
            generateToast("Tienes que seleccionar una imagen")
            return false
        }else{
            return true
        }
    }

    private fun setUpAncientData() {
        urlImFirebase = CurrentUser.currentUser!!.urlImageFirebase
        val nombreUsuario = CurrentUser.currentUser!!.userName
        val nombreReal = CurrentUser.currentUser!!.realName
        val antiguoPassword = CurrentUser.currentUser!!.password
        Glide.with(this)
            .load(CurrentUser.currentUser!!.urlImageFirebase)
            .apply(opcionesGlide(this))
            .transition(transicion)
            .into(binding.ivAccount)
        binding.tietNombre.setText(nombreUsuario)
        binding.tietApellidos.setText(nombreReal)
        binding.tietPassword.setText(antiguoPassword)
    }

    fun opcionesGlide(context: Context): RequestOptions {
        return RequestOptions()
            .placeholder(animacionCarga(context))
    }

    fun animacionCarga(contexto: Context): CircularProgressDrawable {
        val animacion = CircularProgressDrawable(contexto)
        animacion.strokeWidth = 5f
        animacion.centerRadius = 30f
        animacion.start()
        return animacion
    }

    val transicion = DrawableTransitionOptions.withCrossFade(500)

    private fun setUpButtonImageViewBack() {
        binding.ivBack.setOnClickListener {
            val role = CurrentUser.currentUser!!.typeUser
            if(role == "administrador"){
                val intent = Intent(this, MainAdminActivity::class.java)
                startActivity(intent)
            }else{
                val intent = Intent(this, MainClientActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun setUpButtonImageViewGalery() {
        //Cuando le da click en la imagen para guardar imagen del juego
        binding.ivAccount.setOnClickListener {
            buttonGalery = true
            galeryAccess.launch("image/*")
        }
    }

    suspend fun saveImageCover(stoRef: StorageReference, idUser: String, imagen: Uri):String{

        val urlCoverFirebase: Uri = stoRef.child("MagicCraft").child("Image_Cover_User").child(idUser)
            .putFile(imagen).await().storage.downloadUrl.await()

        return urlCoverFirebase.toString()
    }

    private fun isValidName(name: String): Boolean = name.isNotBlank()

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