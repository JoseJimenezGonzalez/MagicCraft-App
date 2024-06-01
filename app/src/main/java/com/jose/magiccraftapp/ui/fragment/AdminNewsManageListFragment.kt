package com.jose.magiccraftapp.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.data.model.News
import com.jose.magiccraftapp.data.viewmodel.NewsViewModel
import com.jose.magiccraftapp.databinding.FragmentAdminNewsManageListBinding
import com.jose.magiccraftapp.ui.adapter.AdapterRecyclerViewNews


class AdminNewsManageListFragment : Fragment() {

    private var _binding: FragmentAdminNewsManageListBinding? = null

    private val binding get() = _binding!!

    private val newsViewModel: NewsViewModel by viewModels()

    private lateinit var recycler: RecyclerView

    private lateinit var newsList: MutableList<News>

    private lateinit var adapter: AdapterRecyclerViewNews

    // Inicialización de DatabaseReference
    var dbRef = FirebaseDatabase.getInstance().reference

    // Inicialización de StorageReference
    var stRef = FirebaseStorage.getInstance().reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminNewsManageListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Codigo
        setUpRecyclerView()
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

        adapter.onItemLongClick = { new ->

            // Las notificaciones no están habilitadas. Muestra un diálogo al usuario.
            val dialogView = layoutInflater.inflate(R.layout.dialog_custom, null)

            val tvTitle = dialogView.findViewById<TextView>(R.id.tvTitle)
            val tvMessage = dialogView.findViewById<TextView>(R.id.tvMessage)
            val btnYes = dialogView.findViewById<Button>(R.id.btnYes)
            val btnNo = dialogView.findViewById<Button>(R.id.btnNo)

            tvTitle.text = "Eliminar noticia"
            tvMessage.text = "¿Estás seguro de que quieres eliminar la noticia?"

            val alertDialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create()

            btnYes.setOnClickListener {
                //Eliminar la noticia
                dbRef.child("MagicCraft").child("News").child(new.idNew).removeValue()
                stRef.child("MagicCraft").child("Image_Cover_News").child(new.idNew).delete()
                newsList.remove(new)
                adapter.notifyDataSetChanged()
                alertDialog.dismiss()
            }

            btnNo.setOnClickListener {
                // El usuario ha rechazado. Cierra el diálogo.
                alertDialog.dismiss()
            }

            alertDialog.show()
        }

    }

    fun showConfirmationDialog(message: String, confirmAction: () -> Unit) {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirmación")
            .setMessage(message)
            .setPositiveButton("Sí") { dialog, _ ->
                confirmAction()
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

}