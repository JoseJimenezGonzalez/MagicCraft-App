package com.jose.magiccraftapp.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.jose.magiccraftapp.data.model.Message
import com.jose.magiccraftapp.data.repository.MessageRepository

class MessageViewModel(application: Application): AndroidViewModel(application) {
    private val repository = MessageRepository(application)

    fun getMessages(idChat: String) : LiveData<MutableList<Message>>{
        return repository.getMessages(idChat)
    }
}