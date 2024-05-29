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
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.data.model.CurrentUser
import com.jose.magiccraftapp.data.model.MyValueFormatter
import com.jose.magiccraftapp.databinding.FragmentClientDeckManageStatisticsBinding
import kotlin.math.max


class ClientDeckManageStatisticsFragment : Fragment() {

    private var _binding: FragmentClientDeckManageStatisticsBinding? = null
    private val binding get() = _binding!!

    private lateinit var mutableListCost: MutableList<Float>

    private lateinit var mutableListApparitionsCost: MutableList<Float>

    private lateinit var mutableListNameCost: MutableList<String>

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
        mutableListNameCost = mutableListOf()
        //Recorro el mazo actual
        CurrentUser.currentDeck!!.cards.forEach { card ->
            val cmc = card.cmc.toFloat()
            val type = card.type
            if(!mutableListCost.contains(cmc) && type != "Land"){
                mutableListCost.add(cmc)
            }
            mutableListCost.sort()
        }
        mutableListCost.forEach { float ->
            mutableListNameCost.add("Cmc ${float.toInt()}")
        }

        Log.e("lista cmc", "${mutableListCost}")
        Log.e("lista cmc string", "$mutableListNameCost")

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

        //Cantidad de barras
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

        // Cambiar el color del texto de los valores en la parte superior de las barras
        barDataSet.setValueTextColor(ContextCompat.getColor(requireContext(), R.color.white))

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
        xAxis.textSize = sizeTextLabel
        //xAxis.labelRotationAngle = -55f
        xAxis.valueFormatter = IndexAxisValueFormatter(mutableListNameCost)
        xAxis.textColor = ContextCompat.getColor(requireContext(), android.R.color.white)

        // Obtiene el eje Y izquierdo del gráfico
        val yAxisLeft = binding.barChart.axisLeft

        // Establece el valor máximo del eje Y a 100
        yAxisLeft.axisMinimum = 0f
        yAxisLeft.axisMaximum = 20f

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




        //Calculo de la media
        val listOfCost: MutableList<Int> = mutableListOf()
        CurrentUser.currentDeck!!.cards.forEach { card ->
            if(card.type != "Land"){
                val numero = card.numberCard
                for (i in 1..numero){
                    listOfCost.add(card.cmc)
                }
            }
        }
        val media = listOfCost.average()
        val mediaFormateada = String.format("%.2f", media)
        binding.tvMediaCmc.text = "El coste de maná medio del mazo es de $mediaFormateada"

        val mediana = median(listOfCost)
        val modal = mode(listOfCost)

        binding.tvMediana.text = "La mediana del mazo es ${mediana}"
        binding.tvModal.text = "El modal del mazo es ${modal}"



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

    fun median(numbers: List<Int>): Double? {
        return if (numbers.isNotEmpty()) {
            val sortedNumbers = numbers.sorted()
            val middle = numbers.size / 2
            if (numbers.size % 2 == 0) {
                (sortedNumbers[middle - 1] + sortedNumbers[middle]) / 2.0
            } else {
                sortedNumbers[middle].toDouble()
            }
        } else {
            null
        }
    }

    // Función para calcular el modal
    fun mode(numbers: List<Int>): Int? {
        return numbers.groupingBy { it }.eachCount().maxByOrNull { it.value }?.key
    }

}