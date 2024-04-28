package com.jose.magiccraftapp.data.model

import android.view.View
import android.widget.TextView
import com.jose.magiccraftapp.R
import com.kizitonwose.calendar.view.ViewContainer

class MonthHeaderContainer(view: View) : ViewContainer(view) {
    val textView: TextView = view.findViewById(R.id.tvMonthYear)
}