package com.jose.magiccraftapp.data.repository

import android.app.Application
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
import com.jose.magiccraftapp.data.model.CurrentUser
import com.jose.magiccraftapp.data.model.User
import com.jose.magiccraftapp.ui.activity.MainAdminActivity
import com.jose.magiccraftapp.ui.activity.MainClientActivity

class UsuarioRepository (application: Application) {

    val usuarioDao: UsuarioDao? = MagicCraftDatabase.getInstance(application)?.usuarioDao()

    val navigateToActivity = MutableLiveData<Class<*>>()

    val messageToast = MutableLiveData("")

    // Inicialización de DatabaseReference
    var dbRef = FirebaseDatabase.getInstance().reference

    // Inicialización de StorageReference
    var stRef = FirebaseStorage.getInstance().reference

    // Inicialización de FirebaseAuth
    var auth = FirebaseAuth.getInstance()

    fun loginAuth(mail: String, password: String) {
        auth.signInWithEmailAndPassword(mail, password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                val userAuth = auth.currentUser
                val id = userAuth!!.uid
                obtainDataUserFromRealTimeDatabase(id)
            }else{
                messageToast.value = "No existe ese usuario en la base de datos"
            }
        }
    }

    fun obtainDataUserFromRealTimeDatabase(id: String) {
        dbRef.child("MagicCraft").child("Users").child(id).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    //Recojo toda la informacion del usuario
                    val user = snapshot.getValue(User::class.java)
                    val typeUser = user!!.typeUser
                    addCurrentUserToCompanionObject(user)
                    routeUserActivity(typeUser)
                }else{
                    println("El usuario con ID $id no existe")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Error al obtener datos del usuario: $error")
            }

        })
    }

    fun routeUserActivity(typeUser: String) {
        if (typeUser == "administrador"){
            navigateToActivity.value = MainAdminActivity::class.java
        }else{
            navigateToActivity.value = MainClientActivity::class.java
        }
    }

    fun addCurrentUserToCompanionObject(user: User) {
        CurrentUser.currentUser = user
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