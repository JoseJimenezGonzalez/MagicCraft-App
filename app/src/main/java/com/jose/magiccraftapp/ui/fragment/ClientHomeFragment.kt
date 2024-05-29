package com.jose.magiccraftapp.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.data.model.CurrentUser
import com.jose.magiccraftapp.data.model.News
import com.jose.magiccraftapp.data.viewmodel.NewsViewModel
import com.jose.magiccraftapp.databinding.FragmentClientHomeBinding
import com.jose.magiccraftapp.ui.activity.EditProfileActivity
import com.jose.magiccraftapp.ui.activity.SettingClientActivity
import com.jose.magiccraftapp.ui.adapter.AdapterRecyclerViewNews
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ClientHomeFragment : Fragment() {

    private var _binding: FragmentClientHomeBinding? = null
    private val binding get() = _binding!!

    private val newsViewModel: NewsViewModel by viewModels()

    private lateinit var recycler: RecyclerView

    private lateinit var newsList: MutableList<News>

    private lateinit var adapter: AdapterRecyclerViewNews

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Codigo
        setUpButtonOpenUrlRules()
        setUpButtonOpenSetting()
        setUpRecyclerView()
        setUpButtonOpenCalendar()
        updateUI()
        setUpButtonEditProfile()
    }

    private fun setUpButtonEditProfile() {
        binding.ivProfile.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }
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

    private fun handleItem(new: News) {
        CurrentUser.currentNew = new
        findNavController().navigate(R.id.action_clientHomeFragment_to_clientNewViewFragment)
    }

    private fun setUpRecyclerView() {
        newsList = mutableListOf()
        adapter = AdapterRecyclerViewNews(newsList)
        apply {
            recycler = binding.rvNews
            recycler.adapter = adapter
            recycler.layoutManager = LinearLayoutManager(context)
        }
        // Observar los cambios en los mazos
        newsViewModel.getAllNews().observe(viewLifecycleOwner) { news ->
            newsList.clear()
            newsList.addAll(news)
            adapter.notifyDataSetChanged()
        }

        adapter.onItemClick = { new ->
            handleItem(new)
        }

    }

    private fun setUpButtonOpenSetting() {
        binding.ivSettings.setOnClickListener {
            val intent = Intent(requireContext(), SettingClientActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setUpButtonOpenCalendar(){
        binding.cvCalendar.setOnClickListener {
            findNavController().navigate(R.id.action_clientHomeFragment_to_clientCalendarFragment)
        }
    }

    private fun setUpButtonOpenUrlRules() {
        binding.cvRules.setOnClickListener {
            findNavController().navigate(R.id.action_clientHomeFragment_to_viewRulesPdfFragment)
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