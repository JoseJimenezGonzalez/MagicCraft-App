package com.jose.magiccraftapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.jose.magiccraftapp.data.dao.UsuarioDao
import com.jose.magiccraftapp.data.database.MagicCraftDatabase
import com.jose.magiccraftapp.data.entity.Usuario
import com.jose.magiccraftapp.data.model.User

class UsuarioRepository (application: Application) {

    val usuarioDao: UsuarioDao? = MagicCraftDatabase.getInstance(application)?.usuarioDao()

    // Inicialización de DatabaseReference
    var dbRef = FirebaseDatabase.getInstance().reference

    // Inicialización de StorageReference
    var stRef = FirebaseStorage.getInstance().reference

    // Inicialización de FirebaseAuth
    var auth = FirebaseAuth.getInstance()

    fun obtainUser(mail: String, password: String): LiveData<User?> {
        val userLive = MutableLiveData<User?>()
        auth.signInWithEmailAndPassword(mail, password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                val userAuth = auth.currentUser
                val id = userAuth?.uid
                if (id != null) {
                    dbRef.child("MagicCraft").child("Users").child(id).addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.exists()){
                                // Recojo toda la informacion del usuario
                                val user = snapshot.getValue(User::class.java)
                                if (user != null) {
                                    val usuario = User(
                                        user.id,
                                        user.userName,
                                        user.realName,
                                        user.mail,
                                        user.password,
                                        user.typeUser,
                                        user.urlImageFirebase
                                    )
                                    userLive.value = usuario
                                } else {
                                    // El usuario con ID $id no existe
                                    userLive.value = null
                                }
                            } else {
                                // El usuario con ID $id no existe
                                userLive.value = null
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                            println("Error al obtener datos del usuario: $error")
                        }
                    })
                } else {
                    // La autenticación fue exitosa, pero userAuth es null
                    userLive.value = null
                }
            } else {
                // La autenticación falló
                userLive.value = null
            }
        }
        return userLive
    }

    fun getUsernames(): LiveData<MutableList<String>> {
        val usernamesLive = MutableLiveData<MutableList<String>>()
        dbRef.child("MagicCraft").child("Users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val usernamesList = mutableListOf<String>()
                    snapshot.children.forEach { snapshotChild ->
                        val user = snapshotChild.getValue(User::class.java)
                        if (user != null) {
                            usernamesList.add(user.userName.lowercase())
                        }
                    }
                    usernamesLive.value = usernamesList
                } else {
                    usernamesLive.value = mutableListOf()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Error al obtener los nombres de usuario: $error")
            }
        })
        return usernamesLive
    }

    fun obtainUsersChat(id: String): LiveData<MutableList<User>>{
        val usersLive = MutableLiveData<MutableList<User>>()
        dbRef.child("MagicCraft").child("Users").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val usersList = mutableListOf<User>()
                    //Recorrer todos los usuarios e ir añadiendolos menos el actual
                    snapshot.children.forEach { snapshotBucle ->
                        val user = snapshotBucle.getValue(User::class.java)
                        if(user != null && user.id != id){
                            usersList.add(user)
                        }
                    }
                    usersLive.value = usersList
                } else {
                    // El usuario con ID $id no existe
                    usersLive.value = mutableListOf()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                println("Error al obtener datos del usuario: $error")
            }
        })
        return usersLive
    }


    fun insert(usuario: Usuario){
        usuarioDao?.insert(usuario)
    }


    fun update(usuario: Usuario){
        usuarioDao?.update(usuario)
    }


    fun delete(usuario: Usuario){
        usuarioDao?.delete(usuario)
    }
}