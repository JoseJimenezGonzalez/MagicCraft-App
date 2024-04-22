package com.jose.magiccraftapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jose.magiccraftapp.data.model.CurrentUser
import com.jose.magiccraftapp.databinding.FragmentClientNewViewBinding


class ClientNewViewFragment : Fragment() {

    private var _binding: FragmentClientNewViewBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientNewViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Codigo
        setUpWebView()
    }

    private fun setUpWebView() {
        val url = CurrentUser.currentNew!!.urlWeb
        binding.webView.apply {
            settings.setSupportZoom(true)
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            loadUrl(url)
        }
    }

}