package com.jose.magiccraftapp.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.jose.magiccraftapp.data.entity.Usuario
import com.jose.magiccraftapp.data.model.User
import com.jose.magiccraftapp.data.repository.UsuarioRepository


class UsuarioViewModel(application: Application): AndroidViewModel(application) {


    private val repository = UsuarioRepository(application)

    val routeToNavigate = MutableLiveData<Class<*>>()

    val toastMessage = MutableLiveData("")

    fun loginAuth(mail: String, password: String){
        repository.loginAuth(mail, password)
    }

    fun obtainDataUserFromRealTimeDatabase(id: String){
        repository.obtainDataUserFromRealTimeDatabase(id)
    }

    fun routeUserActivity(typeUser: String){
        repository.routeUserActivity(typeUser)
    }

    fun addCurrentUserToCompanionObject(user: User) {
        repository.addCurrentUserToCompanionObject(user)
    }


    fun insert(usuario: Usuario){
        repository.insert(usuario)
    }


    fun update(usuario: Usuario){
        repository.update(usuario)
    }


    fun delete(usuario: Usuario){
        repository.delete(usuario)
    }

    init {
        repository.messageToast.observeForever { message ->
            toastMessage.value = message
        }
        repository.navigateToActivity.observeForever { route ->
            routeToNavigate.value = route
        }
    }
}