package com.jose.magiccraftapp.ui.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.data.model.CurrentUser
import com.jose.magiccraftapp.data.model.Deck
import com.jose.magiccraftapp.databinding.FragmentClientDeckEditBinding
import com.jose.magiccraftapp.databinding.FragmentClientDeckManageSeeCardsBinding
import com.jose.magiccraftapp.ui.activity.MainClientActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class ClientDeckEditFragment : Fragment(), CoroutineScope {

    private var _binding: FragmentClientDeckEditBinding? = null
    private val binding get() = _binding!!

    private var urlImagen: Uri? = null

    private lateinit var cover: ImageView

    @Inject
    lateinit var dbRef: DatabaseReference

    @Inject
    lateinit var stoRef: StorageReference

    lateinit var job: Job

    private var nameDeck = ""

    private var formatDeck = ""

    private var urlImFirebase = ""

    private var buttonGalery = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientDeckEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Codigo

        cover = binding.ivImageDeck

        job = Job()

        setUpAncientData()

        setUpButtonImageViewGalery()
        setUpButtonImageViewBack()
        setUpButtonEditDeck()
    }

    private fun setUpAncientData() {
        urlImFirebase = CurrentUser.currentDeck!!.urlImageFirebase
        val nombreDeck = CurrentUser.currentDeck!!.nameDeck
        val nombreFormato = CurrentUser.currentDeck!!.formatDeck
        Glide.with(requireContext())
            .load(CurrentUser.currentDeck!!.urlImageFirebase)
            .apply(opcionesGlide(requireContext()))
            .transition(transicion)
            .into(binding.ivImageDeck)
        binding.tietNameDeck.setText(nombreDeck)
        binding.tietFormatDeck.setText(nombreFormato)
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


    private fun setUpButtonEditDeck() {

        binding.btnSubmitDeck.setOnClickListener {

            //Extraemos los valores de los campos
            nameDeck = binding.tietNameDeck.text.toString()
            formatDeck = binding.tietFormatDeck.text.toString()
            //Obtenemos los chivatos
            val isNameValid = isValidName(nameDeck)
            val isFormatValid = isValidFormat(formatDeck)
            var isImageValid = true
            if(buttonGalery){
                isImageValid = isValidImage()
            }
            //Pintamos si tiene error
            paintErrorName(isNameValid)
            paintErrorFormat(isFormatValid)
            //Validar all campos
            validateAll(isNameValid, isFormatValid, isImageValid)

        }
    }

    private fun validateAll(nameValid: Boolean, formatValid: Boolean, imageValid: Boolean) {
        if(nameValid && formatValid && imageValid){
            editDeck()
        }
    }

    private fun editDeck() {
        val idUsuario = CurrentUser.currentDeck!!.idUserDeck
        val idDeck = CurrentUser.currentDeck!!.idDeck
        val cards = CurrentUser.currentDeck!!.cards
        var urlImageFirebase = CurrentUser.currentDeck!!.urlImageFirebase
        launch {
            if (buttonGalery){
                urlImageFirebase = saveImageCover(stoRef, idUsuario, idDeck, urlImagen!!)

            }
            dbRef.child("MagicCraft").child("Decks").child(idUsuario).child(idDeck).setValue(
                Deck(
                    idUsuario,
                    idDeck,
                    nameDeck,
                    formatDeck,
                    urlImageFirebase,
                    cards = cards
                )
            )
        }
        //Cambiamos la informacion en el companion object
        CurrentUser.currentDeck = Deck(
            idUserDeck = idUsuario,
            idDeck = idDeck,
            nameDeck = nameDeck,
            formatDeck = formatDeck,
            urlImageFirebase = urlImageFirebase,
            cards = cards
        )
        generateToast("Se ha cambiado correctamente la información")
    }


    private fun setUpButtonImageViewBack() {
        binding.ivBack.setOnClickListener {
            findNavController().navigate(R.id.action_clientDeckEditFragment_to_clientDeckManageFragment)
        }
    }

    private fun setUpButtonImageViewGalery() {
        //Cuando le da click en la imagen para guardar imagen del juego
        binding.ivImageDeck.setOnClickListener {
            buttonGalery = true
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
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
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