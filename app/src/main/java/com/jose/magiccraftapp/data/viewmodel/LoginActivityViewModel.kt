package com.jose.magiccraftapp.data.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jose.magiccraftapp.data.repository.LoginActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginActivityViewModel @Inject constructor(private val repository: LoginActivityRepository): ViewModel() {

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