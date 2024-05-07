package com.jose.magiccraftapp.ui.fragment

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.data.model.DayViewContainer
import com.jose.magiccraftapp.data.model.Event
import com.jose.magiccraftapp.data.model.MonthHeaderContainer
import com.jose.magiccraftapp.data.viewmodel.EventViewModel
import com.jose.magiccraftapp.databinding.FragmentAdminCalendarBinding
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


class AdminCalendarFragment : Fragment() {

    private var _binding: FragmentAdminCalendarBinding? = null

    private val binding get() = _binding!!

    private val eventViewModel: EventViewModel by viewModels()

    // Inicialización de DatabaseReference
    var dbRef = FirebaseDatabase.getInstance().reference

    // Inicialización de StorageReference
    var stRef = FirebaseStorage.getInstance().reference

    // Inicialización de FirebaseAuth
    var auth = FirebaseAuth.getInstance()

    var listEvent: MutableList<Event> = mutableListOf()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        eventViewModel.getAllEvents().observe(viewLifecycleOwner, Observer { events ->
            // Aquí puedes manejar los eventos cuando estén disponibles
            Log.e("AdminHomeFragment1", events.toString())
            listEvent = events
            Log.e("listEvent", listEvent.toString())
            setUpCalendar()
        })

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUpCalendar() {
        val currentMonth = YearMonth.now()
        binding.calendarView.apply {
            setup(currentMonth.minusMonths(5), currentMonth.plusMonths(5), LocalDate.now().dayOfWeek)
            scrollToMonth(currentMonth)
        }

        // Configuración del encabezado del mes
        binding.calendarView.monthHeaderBinder = object :
            MonthHeaderFooterBinder<MonthHeaderContainer> {
            override fun bind(container: MonthHeaderContainer, month: CalendarMonth) {
                val headerText = "${month.yearMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${month.yearMonth.year}"
                container.textView.text = headerText

                // Configuración de los títulos de los días de la semana
                val titlesContainer = container.view.findViewById<ViewGroup>(R.id.titlesContainer)
                val daysOfWeek = listOf("L", "M", "X", "J", "V", "S", "D")
                titlesContainer.children
                    .map { it as TextView }
                    .forEachIndexed { index, textView ->
                        val title = daysOfWeek[index]
                        textView.text = title
                    }
            }

            override fun create(view: View) = MonthHeaderContainer(view)
        }

        // Configuración de los días de la semana
        binding.calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            @RequiresApi(Build.VERSION_CODES.O)
            override fun bind(container: DayViewContainer, day: CalendarDay) {

                //Cambia el color de los dias segunb sea de este mes o no
                if (day.position == DayPosition.MonthDate) {
                    container.textView.setTextColor(Color.BLACK)
                } else {
                    container.textView.setTextColor(Color.LTGRAY)
                }

                //Pintra los dias en el calendario
                container.textView.text = day.date.dayOfMonth.toString()

                // Limpiar el contenedor de eventos antes de añadir nuevos layouts
                container.view.findViewById<LinearLayout>(R.id.contenedorEventos).removeAllViews()

                //Pintar los eventos en el calendario
                val dayEvents = listEvent.filter { event ->
                    val fechaString = event.fecha
                    val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
                    val fechaLocalDate = LocalDate.parse(fechaString, formatter)
                    fechaLocalDate == day.date
                }
                dayEvents.forEach { event ->
                    // Inflar el layout personalizado
                    val layout = LayoutInflater.from(container.view.context).inflate(R.layout.calendar_day_event, null, false)
                    // Establecer el título del evento
                    layout.findViewById<TextView>(R.id.eventTitle).text = event.nombre
                    // Añadir el layout personalizado al contenedor de eventos
                    container.view.findViewById<LinearLayout>(R.id.contenedorEventos).addView(layout)
                }

                // Establecer un OnClickListener en el contenedor del día
                container.view.setOnClickListener {
                    val date = day.date
                    val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
                    val fechaString = date.format(formatter)
                    val lista = getListEventTargetDate(fechaString)
                    Log.e("AdminCalendarFragmentlista", lista.toString())
                    if(lista.isNotEmpty()){
                        //Mostramos la lista en el recycler view
                        //Pasamos la fecha para filtrar luego los eventos
                        val bundle = Bundle()
                        bundle.putString("fechaString", fechaString)
                        findNavController().navigate(R.id.action_adminCalendarFragment_to_adminCalendarEventFragment, bundle)
                    }
                }

            }
        }
    }

    fun getListEventTargetDate(date: String): MutableList<Event>{
         return listEvent.filter { event ->
            date == event.fecha
        }.toMutableList()
    }

}