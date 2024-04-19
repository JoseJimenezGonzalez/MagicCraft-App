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

    fun obtainUser(mail: String, password: String): LiveData<Usuario?> {
        val userLive = MutableLiveData<Usuario?>()
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
                                    val usuario = Usuario(
                                        user.id,
                                        user.mail,
                                        user.name,
                                        user.surname,
                                        user.password,
                                        user.typeUser
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