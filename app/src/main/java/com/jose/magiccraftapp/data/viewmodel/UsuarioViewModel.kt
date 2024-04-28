package com.jose.magiccraftapp.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.jose.magiccraftapp.data.entity.Usuario
import com.jose.magiccraftapp.data.model.User
import com.jose.magiccraftapp.data.repository.UsuarioRepository


class UsuarioViewModel(application: Application): AndroidViewModel(application) {


    private val repository = UsuarioRepository(application)

    fun obtainUser(mail: String, password: String): LiveData<User?>{
        return repository.obtainUser(mail, password)
    }

    fun obtainUsersChat(id: String): LiveData<MutableList<User>>{
        return repository.obtainUsersChat(id)
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

}