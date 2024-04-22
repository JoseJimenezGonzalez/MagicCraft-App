package com.jose.magiccraftapp.ui.fragment

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.data.model.News
import com.jose.magiccraftapp.databinding.FragmentAdminNewsBinding
import com.jose.magiccraftapp.databinding.FragmentAdminNewsManageAddBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class AdminNewsManageAddFragment : Fragment(), CoroutineScope {

    private var _binding: FragmentAdminNewsManageAddBinding? = null

    private val binding get() = _binding!!

    private var urlImagen: Uri? = null

    private lateinit var cover: ImageView

    @Inject
    lateinit var dbRef: DatabaseReference

    @Inject
    lateinit var stoRef: StorageReference

    lateinit var job: Job

    private var title = ""

    private var subTittle = ""

    private var urlNew = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminNewsManageAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Codigo

        cover = binding.ivImageNews

        job = Job()

        setUpButtonImageViewGalery()
        setUpButtonAddNews()

    }

    private fun setUpButtonAddNews() {
        binding.btnSubmitNews.setOnClickListener {
            //Extraemos los valores de los campos
            title = binding.tietTitle.text.toString()
            subTittle = binding.tietSubTittle.text.toString()
            urlNew = binding.tietUrl.text.toString()
            //Obtenemos los chivatos
            val isTitleValid = isValidName(title)
            val isSubTittleValid = isValidSubTittle(subTittle)
            val isUrlValid = isValidName(urlNew)
            val isImageValid = isValidImage()
            //Pintamos si tiene error
            paintErrorTittle(isTitleValid)
            paintErrorSubTittle(isSubTittleValid)
            //Validar all campos
            validateAll(isTitleValid, isSubTittleValid, isImageValid)

        }
    }

    private fun validateAll(titleValid: Boolean, subTittleValid: Boolean, imageValid: Boolean) {
        if(titleValid && subTittleValid && imageValid){
            val idNew = dbRef.child("MagicCraft").child("News").push().key!!
            registerNew(idNew)
        }
    }

    private fun registerNew(idNew: String) {
        launch {
            val urlImageFirebase = saveImageCover(stoRef, idNew, urlImagen!!)
            dbRef.child("MagicCraft").child("News").child(idNew).setValue(
                News(
                    idNew,
                    urlImageFirebase,
                    urlNew,
                    title,
                    subTittle
                )
            )
        }
        generateToast("Noticia añadida con éxito")
    }

    suspend fun saveImageCover(stoRef: StorageReference, idNew: String, imagen: Uri): String {
        val urlCoverFirebase: Uri = stoRef.child("MagicCraft").child("Image_Cover_News").child(idNew)
            .putFile(imagen).await().storage.downloadUrl.await()

        return urlCoverFirebase.toString()
    }

    private fun paintErrorSubTittle(subTittleValid: Boolean) {
        if(subTittleValid){
            binding.tietSubTittle.error = null
        }else{
            binding.tietSubTittle.error = "No puede estar vacío"
        }
    }

    private fun paintErrorTittle(titleValid: Boolean) {
        if(titleValid){
            binding.tietTitle.error = null
        }else{
            binding.tietTitle.error = "No puede estar vacío"
        }
    }

    private fun isValidSubTittle(subTittle: String): Boolean = subTittle.isNotBlank()

    private fun isValidImage(): Boolean {
        if(urlImagen == null){
            generateToast("Tienes que seleccionar una imagen")
            return false
        }else{
            return true
        }
    }

    private fun generateToast(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    private fun isValidName(title: String): Boolean = title.isNotBlank()

    private fun setUpButtonImageViewGalery() {
        //Cuando le da click en la imagen para guardar imagen del juego
        binding.ivImageNews.setOnClickListener {
            galeryAccess.launch("image/*")
        }
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