package com.jose.magiccraftapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.databinding.FragmentClientDeckManageStatisticsBinding
import com.jose.magiccraftapp.datasource.model.CurrentUser
import com.jose.magiccraftapp.datasource.model.MyValueFormatter
import kotlin.math.max


class ClientDeckManageStatisticsFragment : Fragment() {

    private var _binding: FragmentClientDeckManageStatisticsBinding? = null
    private val binding get() = _binding!!

    private lateinit var mutableListCost: MutableList<Float>

    private lateinit var mutableListApparitionsCost: MutableList<Float>

    //Colores
    private lateinit var colors: MutableList<Int>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientDeckManageStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        colors = mutableListOf(
            ContextCompat.getColor(requireContext(), R.color.m200_rosa),
            ContextCompat.getColor(requireContext(), R.color.m200_rosa_oscuro),
            ContextCompat.getColor(requireContext(), R.color.m200_purpura),
            ContextCompat.getColor(requireContext(), R.color.m200_lila),
            ContextCompat.getColor(requireContext(), R.color.m200_verde),
            ContextCompat.getColor(requireContext(), R.color.m200_amarillo),
            ContextCompat.getColor(requireContext(), R.color.m200_naranja),
            ContextCompat.getColor(requireContext(), R.color.m200_azul)
        )

    }

    override fun onResume() {
        super.onResume()

        //Codigo
        mutableListCost = mutableListOf()
        //Recorro el mazo actual
        CurrentUser.currentDeck!!.cards.forEach { card ->
            val cmc = card.cmc.toFloat()
            val type = card.type
            if(!mutableListCost.contains(cmc) && type != "Land"){
                mutableListCost.add(cmc)
            }
            mutableListCost.sort()
        }

        Log.e("lista cmc", "${mutableListCost}")

        mutableListApparitionsCost = MutableList(mutableListCost.size) { 0.0f }
        //Recorro el mazo actual
        for (i in 0 until mutableListApparitionsCost.size){
            var contador: Int = 0
            CurrentUser.currentDeck!!.cards.forEach { card ->
                if(mutableListCost[i].toInt() == card.cmc && card.type != "Land"){
                    contador += card.numberCard
                }
            }
            mutableListApparitionsCost[i] = contador.toFloat()
        }

        Log.e("lista de apariciones", "${mutableListApparitionsCost}")

        binding.barChart.setTouchEnabled(false)

        //Cantidad de notas que hay en el curso
        val numberOfGrades = mutableListCost.size
        //1º. Genero las posiciones
        val listOfPositions = generatePositions(numberOfGrades)
        //2º. Genero los colores
        val listOfColors = generateColors(numberOfGrades)
        //3º. Genero las notas
        val listOfGrades = mutableListApparitionsCost
        //4º. Genero entries
        val listOfEntries = generateEntries(listOfPositions, listOfGrades)
        //5º. Genero los labels
        val listOfLabels = mutableListCost
        //6º. Tamaño del texto de los labels
        val sizeTextLabel = calculateTextSizeOfLabels(numberOfGrades)
        //7º. Tamaño del texto que esta encima de las barras indicando las notas
        val sizeTextIndicator = calculateTextSizeOfIndicator(numberOfGrades)

        val barDataSet = BarDataSet(listOfEntries, "Dataset Label")
        barDataSet.valueFormatter = MyValueFormatter()

        val data = BarData(barDataSet)
        binding.barChart.data = data
        binding.barChart.legend.isEnabled = false // Oculta la leyenda
        binding.barChart.description.isEnabled = false // Oculta la descripción

        barDataSet.colors = listOfColors

        val xAxis = binding.barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.labelCount = listOfLabels.size
        xAxis.textSize = sizeTextLabel//Tamaño de texto de los niveles(el que aparece debajo de las barras)
        //xAxis.valueFormatter = IndexAxisValueFormatter(listOfLabels)

        // Obtiene el eje Y izquierdo del gráfico
        val yAxisLeft = binding.barChart.axisLeft

        // Establece el valor máximo del eje Y a 100
        yAxisLeft.axisMinimum = 0f
        yAxisLeft.axisMaximum = 15f

        binding.barChart.animateY(2500, Easing.EaseOutCubic)//Animacion

        // Deshabilita las líneas de cuadrícula en los ejes X y Y
        val yAxisRight = binding.barChart.axisRight
        xAxis.setDrawGridLines(false)
        yAxisLeft.setDrawGridLines(false)
        yAxisRight.setDrawGridLines(false)

        // Deshabilita las líneas del eje Y
        yAxisLeft.setDrawAxisLine(false)
        yAxisRight.setDrawAxisLine(false)

        // Deshabilita las etiquetas del eje Y
        yAxisLeft.setDrawLabels(false)
        yAxisRight.setDrawLabels(false)

        // Tamaño notas examenes
        barDataSet.valueTextSize = sizeTextIndicator

        binding.barChart.invalidate() // refresca para ver los cambios

    }

    private fun generatePositions(numberOfElements: Int): MutableList<Float>{
        val positions = mutableListOf<Float>()
        for (i in 0 until numberOfElements){
            positions.add(i.toFloat())
        }
        return positions
    }

    private fun generateColors(numberOfElements: Int): MutableList<Int>{
        val colorsResult = mutableListOf<Int>()
        for(i in 0 until numberOfElements){
            colorsResult.add(colors[i])
        }
        return colorsResult
    }

    private fun generateGrades(grades: MutableList<Double>): MutableList<Float> {
        return grades.map { grade -> grade.toFloat() }.toMutableList()
    }

    private fun calculateTextSizeOfLabels(numberOfBars: Int): Float {
        // Define un tamaño de texto base y un factor de disminución
        val baseTextSize = 12f
        val decreaseFactor = 0.5f

        // Calcula el tamaño de texto disminuyendo el tamaño base en función del número de barras
        val textSize = baseTextSize - decreaseFactor * numberOfBars

        // Asegúrate de que el tamaño del texto no sea menor que un mínimo
        val minTextSize = 8f
        return max(textSize, minTextSize)
    }

    private fun calculateTextSizeOfIndicator(numberOfBars: Int): Float {
        // Define un tamaño de texto base y un factor de disminución
        val baseTextSize = 18f
        val decreaseFactor = 0.5f

        // Calcula el tamaño de texto disminuyendo el tamaño base en función del número de barras
        val textSize = baseTextSize - decreaseFactor * numberOfBars

        // Asegúrate de que el tamaño del texto no sea menor que un mínimo
        val minTextSize = 10f
        return max(textSize, minTextSize)
    }

    private fun generateEntries(position: MutableList<Float>, grade: MutableList<Float>): MutableList<BarEntry>{
        val numberOfElements = position.size
        val entries = mutableListOf<BarEntry>()
        for (i in 0 until numberOfElements){
            entries.add(BarEntry(position[i], grade[i]))
        }
        return entries
    }

}