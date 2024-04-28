package com.jose.magiccraftapp.data.model

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.databinding.ItemBottomsheetBinding

class ModalBottomSheetDialog  : BottomSheetDialogFragment() {

    private lateinit var binding : ItemBottomsheetBinding

    private var imageUrl: String? = null

    fun setImageUrl(url: String) {
        this.imageUrl = url
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.item_bottomsheet, container, false)
        val imageView: ImageView = view.findViewById(R.id.ivCardBottomSheet)

        Glide.with(this)
            .load(imageUrl)
            .into(imageView)

        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // used to show the bottom sheet dialog
        dialog?.setOnShowListener { it ->
            val d = it as BottomSheetDialog
            val bottomSheet = d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return super.onCreateDialog(savedInstanceState)
    }

    companion object {
        const val TAG = "ModalBottomSheetDialog"
    }
}