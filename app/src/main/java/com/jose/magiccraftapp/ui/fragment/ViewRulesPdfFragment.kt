package com.jose.magiccraftapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jose.magiccraftapp.databinding.FragmentViewRulesPdfBinding


class ViewRulesPdfFragment : Fragment() {

    private var _binding: FragmentViewRulesPdfBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewRulesPdfBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        setUpWebView()
    }

    private fun setUpWebView() {

        val urlGoogleDrive = "https://drive.google.com/viewerng/viewer?embedded=true&url="
        val urlPdf = "https://media.wizards.com/images/magic/resources/rules/SP_MTGM14_Rulebook_Web.pdf"
        val finalUrl = urlGoogleDrive + urlPdf
        binding.webView.apply {
            settings.setSupportZoom(true)
            settings.domStorageEnabled = true
            settings.javaScriptEnabled = true
            loadUrl(finalUrl)
        }
    }
}