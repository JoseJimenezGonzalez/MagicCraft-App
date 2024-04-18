package com.jose.magiccraftapp.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.jose.magiccraftapp.data.repository.LoginActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginActivityViewModel @Inject constructor(private val repository: LoginActivityRepository, application: Application): AndroidViewModel(application) {

    val routeToNavigate = MutableLiveData<Class<*>>()
    val toastMessage = MutableLiveData("")

    init {
        repository.messageToast.observeForever { message ->
            toastMessage.value = message
        }
        repository.navigateToActivity.observeForever { route ->
            routeToNavigate.value = route
        }
    }

    fun loginAuth(mail: String, password: String){
        repository.loginAuth(mail, password)
    }
}