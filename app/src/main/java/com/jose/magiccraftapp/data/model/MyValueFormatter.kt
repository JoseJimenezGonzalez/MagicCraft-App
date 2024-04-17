package com.jose.magiccraftapp.data.model

import com.github.mikephil.charting.formatter.ValueFormatter

class MyValueFormatter() : ValueFormatter() {
    override fun getFormattedValue(value: Float): String = "${value.toInt()}"
}