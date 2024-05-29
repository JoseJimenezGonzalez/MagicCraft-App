package com.jose.magiccraftapp.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.data.model.CurrentUser
import com.jose.magiccraftapp.databinding.FragmentAdminHomeBinding
import com.jose.magiccraftapp.ui.activity.SettingAdminActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AdminHomeFragment : Fragment() {

    private var _binding: FragmentAdminHomeBinding? = null
    private val binding get() = _binding!!

    // Inicialización de DatabaseReference
    var dbRef = FirebaseDatabase.getInstance().reference

    // Inicialización de StorageReference
    var stRef = FirebaseStorage.getInstance().reference

    // Inicialización de FirebaseAuth
    var auth = FirebaseAuth.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpButtonCalendar()
        setUpButtonOpenUrlRules()
        setUpButtonSetting()
        updateUI()

    }

    private fun updateUI() {

        //Poner la foto
        Glide.with(requireContext())
            .load(CurrentUser.currentUser!!.urlImageFirebase)
            .apply(opcionesGlide(requireContext()))
            .transition(transicion)
            .into(binding.ivProfile)
        //Poner el nombre
        binding.tvNameUser.text = CurrentUser.currentUser!!.name
        //Poner la fecha
        val fechaStringActual = obtenerFechaActual()
        binding.tvDate.text = fechaStringActual
    }

    private fun setUpButtonSetting(){
        binding.ivSettings.setOnClickListener {
            val intent = Intent(requireContext(), SettingAdminActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setUpButtonOpenUrlRules() {
        binding.cvReglamento.setOnClickListener {
            findNavController().navigate(R.id.action_adminHomeFragment_to_viewRulesPdfFragment2)
        }
    }

    private fun setUpButtonCalendar() {
        binding.cvCalendar.setOnClickListener {
            findNavController().navigate(R.id.action_adminHomeFragment_to_adminCalendarFragment)
        }
    }

    private val transicion = DrawableTransitionOptions.withCrossFade(500)

    private fun opcionesGlide(context: Context): RequestOptions {
        return RequestOptions()
            .placeholder(animationLoading(context))

    }

    private fun animationLoading(context: Context): CircularProgressDrawable {
        val animation = CircularProgressDrawable(context)
        animation.strokeWidth = 5f
        animation.centerRadius = 30f
        animation.start()
        return animation
    }

    fun obtenerFechaActual(): String {
        val fecha = Calendar.getInstance().time
        val formato = SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy", Locale("es", "ES"))
        return formato.format(fecha)
    }

}