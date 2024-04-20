package com.jose.magiccraftapp.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.jose.magiccraftapp.data.model.Event
import com.jose.magiccraftapp.data.repository.EventRepository

class EventViewModel (application: Application): AndroidViewModel(application) {
    private val repository = EventRepository(application)

    fun getAllEvents(): LiveData<MutableList<Event>>{
        return repository.getAllEvents()
    }
}