package com.jose.magiccraftapp.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import com.jose.magiccraftapp.data.model.Event
import com.jose.magiccraftapp.databinding.FragmentAdminEventManageAddBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


@AndroidEntryPoint
class AdminEventManageAddFragment : Fragment() , CoroutineScope {

    private var _binding: FragmentAdminEventManageAddBinding? = null
    private val binding get() = _binding!!

    private var urlImagen: Uri? = null

    private lateinit var foto: ImageView

    @Inject
    lateinit var dbRef: DatabaseReference

    @Inject
    lateinit var stoRef: StorageReference

    lateinit var job: Job

    private lateinit var listaEventos: MutableList<Event>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminEventManageAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Codigo
        foto = binding.ivImagen

        job = Job()

        listaEventos = obtenerListaEventos(dbRef)

        configurarBotonImageViewAccesoGaleria()
        configurarDatePicker()
        configurarBotonRegistrarEvento()

    }

    private fun configurarBotonRegistrarEvento() {
        binding.btnAgregarEvento.setOnClickListener {
            val nombre = binding.tietNombreEvento.text.toString().trim()
            val formato = binding.tietFormatoTorneo.text.toString().trim()
            val fechaTorneo = binding.tietFechaEvento.text.toString().trim()
            val precioEvento = binding.tietPrecioEvento.text.toString().trim()
            val aforoEvento = binding.tietAforoEvento.text.toString().trim()
            //Booleanos para indicar si es correcto
            var esNombreCorrecto = false
            var esFormatoCorrecto = false
            var esFechaCorrecto = false
            var esPrecioCorrecto = false
            var esAforoCorrecto = false
            var esFotoCorrecta = false
            var existeEvento = false
            //Condicionales
            if(nombre.isNotBlank()){
                binding.tilNombreEvento.error = null
                esNombreCorrecto = true
            }else{
                binding.tilNombreEvento.error = "El nombre no puede estar vacío"
            }

            if(formato.isNotBlank()){
                binding.tilFormatoTorneo.error = null
                esFormatoCorrecto = true
            }else{
                binding.tilFormatoTorneo.error = "El formato no puede estar vacío"
            }

            if(fechaTorneo.isNotBlank()){
                binding.tilFechaEvento.error = null
                esFechaCorrecto = true
            }else{
                binding.tilFechaEvento.error = "La fecha no puede estar vacía"
            }

            if(precioEvento.isNotBlank()){
                binding.tilPrecioEvento.error = null
                esPrecioCorrecto = true
            }else{
                binding.tilPrecioEvento.error = "El precio no puede estar vacío"
            }

            if(aforoEvento.isNotBlank()){
                binding.tilAforoEvento.error = null
                esAforoCorrecto = true
            }else{
                binding.tilAforoEvento.error = "El aforo no puede estar vacío"
            }

            if(urlImagen == null){
                Toast.makeText(context, "Falta la seleccionar la imagen", Toast.LENGTH_SHORT).show()
            }else{
                esFotoCorrecta = true
            }
            //No puede haber dos eventos con el mismo nombre y misma fecha
            if(existeEvento(listaEventos, nombre, fechaTorneo)){
                Toast.makeText(context, "Ya existe ese evento", Toast.LENGTH_SHORT).show()
                existeEvento = true
            }
            //Si todas las condiciones estan bien
            if(esAforoCorrecto && esFechaCorrecto && esFormatoCorrecto && esPrecioCorrecto && esNombreCorrecto && esFotoCorrecta && !existeEvento){
                val idEvento = dbRef.child("MagicCraft").child("Events").push().key
                registrarEventoEnBaseDatos(idEvento, nombre, formato, fechaTorneo, precioEvento, aforoEvento)
                Toast.makeText(context, "Se ha introducido el evento correctamente", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun registrarEventoEnBaseDatos(
        idEvento: String?,
        nombre: String,
        formato: String,
        fechaTorneo: String,
        precioEvento: String,
        aforoEvento: String
    ) {
        launch {
            val urlImageFirebase = guardarImagenCover(stoRef, idEvento!!, urlImagen!!)
            dbRef.child("MagicCraft").child("Events").child(idEvento).setValue(
                Event(
                    id = idEvento,
                    nombre = nombre,
                    formato = formato,
                    fecha = fechaTorneo,
                    precio = precioEvento.toDouble(),
                    aforo = aforoEvento.toInt(),
                    aforoOcupado = 0,
                    urlImagenEvento = urlImageFirebase,
                    idUsers = mutableListOf(),
                    messageList = mutableListOf()
                )
            )
        }
    }

    private fun configurarDatePicker() {
        //Fecha lanzamiento
        binding.tietFechaEvento.setOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker()
            val picker = builder.build()

            picker.addOnPositiveButtonClickListener { selectedDateInMillis ->
                val selectedDate = Date(selectedDateInMillis)
                binding.tietFechaEvento.setText(obtenerFechaLanzamientoFormateada(selectedDate))
            }

            picker.show(requireActivity().supportFragmentManager, "fecha")
        }
    }

    private fun obtenerFechaLanzamientoFormateada(fechaLanzamiento: Date): String {
        return SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(fechaLanzamiento)
    }

    private fun obtenerListaEventos(dbRef: DatabaseReference): MutableList<Event> {

        val lista = mutableListOf<Event>()

        dbRef.child("MagicCraft")
            .child("Events")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach{hijo : DataSnapshot ->
                        val pojoEvento = hijo.getValue(Event::class.java)
                        lista.add(pojoEvento!!)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    println(error.message)
                }
            })

        return lista
    }
    fun existeEvento(listaEventos : List<Event>, nombre:String, fecha: String):Boolean{
        return listaEventos.any{ it.nombre.lowercase()==nombre.lowercase() && it.fecha == fecha}
    }

    suspend fun guardarImagenCover(stoRef: StorageReference, id:String, imagen: Uri):String{

        val urlCoverFirebase: Uri = stoRef.child("MagicCraft").child("Image_Cover_Event").child(id)
            .putFile(imagen).await().storage.downloadUrl.await()

        return urlCoverFirebase.toString()
    }
    private fun configurarBotonImageViewAccesoGaleria() {
        //Cuando le da click en la imagen para guardar imagen del juego
        binding.ivImagen.setOnClickListener {
            accesoGaleria.launch("image/*")
        }
    }
    private val accesoGaleria =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                urlImagen = uri
                foto.setImageURI(uri)
            }
        }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job
}