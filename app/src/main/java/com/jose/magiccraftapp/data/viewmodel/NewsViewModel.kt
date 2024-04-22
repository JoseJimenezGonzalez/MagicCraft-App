package com.jose.magiccraftapp.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.jose.magiccraftapp.data.model.News
import com.jose.magiccraftapp.data.repository.NewsRepository

class NewsViewModel (application: Application): AndroidViewModel(application){
    private val repository = NewsRepository(application)

    fun getAllNews(): LiveData<MutableList<News>>{
        return repository.getAllNews()
    }
}