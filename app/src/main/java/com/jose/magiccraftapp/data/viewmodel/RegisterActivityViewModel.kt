package com.jose.magiccraftapp.data.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jose.magiccraftapp.data.model.UserForm
import com.jose.magiccraftapp.data.repository.RegisterActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterActivityViewModel @Inject constructor(private val repository: RegisterActivityRepository): ViewModel() {
    //Mensaje del toast
    val messageToast = MutableLiveData("")

    init {
        repository.toastMessage.observeForever { message ->
            messageToast.value = message
        }
    }
    //Funciones
    fun registerUserAuth(userForm: UserForm) {
        repository.registerUserAuth(userForm)
    }

}