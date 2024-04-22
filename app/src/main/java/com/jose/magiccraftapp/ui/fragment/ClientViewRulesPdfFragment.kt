package com.jose.magiccraftapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.data.model.User
import com.jose.magiccraftapp.data.viewmodel.UsuarioViewModel
import com.jose.magiccraftapp.databinding.FragmentAdminChatBinding
import com.jose.magiccraftapp.databinding.FragmentClientViewRulesPdfBinding
import com.jose.magiccraftapp.ui.adapter.AdapterRecyclerViewChat


class ClientViewRulesPdfFragment : Fragment() {

    private var _binding: FragmentClientViewRulesPdfBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientViewRulesPdfBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        setUpWebView()
    }

    private fun setUpWebView() {

        val urlGoogleDrive = "https://drive.google.com/viewerng/viewer?embedded=true&url="
        val urlPdf = "https://media.wizards.com/2024/downloads/MagicCompRules%2004102024.pdf"
        val finalUrl = urlGoogleDrive + urlPdf
        binding.webView.apply {
            settings.setSupportZoom(true)
            settings.domStorageEnabled = true
            settings.javaScriptEnabled = true
            loadUrl(finalUrl)
        }
    }
}