package com.jose.magiccraftapp.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.databinding.ActivityClientCreateDeckBinding
import com.jose.magiccraftapp.datasource.model.CurrentUser
import com.jose.magiccraftapp.datasource.model.Deck
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class ClientCreateDeckActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var binding: ActivityClientCreateDeckBinding

    private var urlImagen: Uri? = null

    private lateinit var cover: ImageView

    @Inject
    lateinit var dbRef: DatabaseReference

    @Inject
    lateinit var stoRef: StorageReference

    lateinit var job: Job

    private var nameDeck = ""

    private var formatDeck = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityClientCreateDeckBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Codigo

        cover = binding.ivImageDeck

        job = Job()

        setUpButtonImageViewGalery()
        setUpButtonImageViewBack()
        setUpButtonAddDeck()

    }

    private fun setUpButtonAddDeck() {

        binding.btnSubmitDeck.setOnClickListener {

            //Extraemos los valores de los campos
            nameDeck = binding.tietNameDeck.text.toString()
            formatDeck = binding.tietFormatDeck.text.toString()
            //Obtenemos los chivatos
            val isNameValid = isValidName(nameDeck)
            val isFormatValid = isValidFormat(formatDeck)
            val isImageValid = isValidImage()
            //Pintamos si tiene error
            paintErrorName(isNameValid)
            paintErrorFormat(isFormatValid)
            //Validar all campos
            validateAll(isNameValid, isFormatValid, isImageValid)
            
        }
    }

    private fun validateAll(nameValid: Boolean, formatValid: Boolean, imageValid: Boolean) {
        if(nameValid && formatValid && imageValid){
            val idUser = CurrentUser.currentUser!!.id
            val idDeck = dbRef.child("MagicCraft").child("Decks").child(idUser).push().key
            registerDeck(idUser, idDeck)
        }
    }

    private fun registerDeck(idUser: String, idDeck: String?) {
        launch {
            val urlImageFirebase = saveImageCover(stoRef, idUser, idDeck!!, urlImagen!!)
            dbRef.child("MagicCraft").child("Decks").child(idUser).child(idDeck).setValue(
                Deck(
                    idUser,
                    idDeck,
                    nameDeck,
                    formatDeck,
                    urlImageFirebase,
                )
            )
        }
        generateToast("Mazo añadido con exito")
        val intent = Intent(this, MainClientActivity::class.java)
        startActivity(intent)
    }


    private fun setUpButtonImageViewBack() {
        binding.ivBack.setOnClickListener {
            val intent = Intent(this, MainClientActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setUpButtonImageViewGalery() {
        //Cuando le da click en la imagen para guardar imagen del juego
        binding.ivImageDeck.setOnClickListener {
            galeryAccess.launch("image/*")
        }
    }

    private fun isValidName(name: String): Boolean = name.isNotBlank()

    private fun isValidFormat(format: String): Boolean = format.isNotBlank()

    private fun isValidImage(): Boolean{
        if(urlImagen == null){
            generateToast("Tienes que seleccionar una imagen")
            return false
        }else{
            return true
        }
    }

    suspend fun saveImageCover(stoRef: StorageReference, idUser: String, idDeck:String, imagen: Uri):String{

        val urlCoverFirebase: Uri = stoRef.child("MagicCraft").child("Image_Cover_Deck").child(idUser).child(idDeck)
            .putFile(imagen).await().storage.downloadUrl.await()

        return urlCoverFirebase.toString()
    }

    private fun paintErrorName(isNameValid: Boolean){
        if(isNameValid){
            binding.tietNameDeck.error = null
        }else{
            binding.tietNameDeck.error = "No puede estar vacío"
        }
    }

    private fun paintErrorFormat(isFormatValid: Boolean){
        if(isFormatValid){
            binding.tietFormatDeck.error = null
        }else{
            binding.tietFormatDeck.error = "No puede estar vacío"
        }
    }

    private fun generateToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

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