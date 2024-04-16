package com.jose.magiccraftapp.datasource.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.jose.magiccraftapp.datasource.model.CurrentUser
import com.jose.magiccraftapp.datasource.model.User
import com.jose.magiccraftapp.ui.activity.MainAdminActivity
import com.jose.magiccraftapp.ui.activity.MainClientActivity
import javax.inject.Inject

class LoginActivityRepository @Inject constructor(private val dbRef: DatabaseReference, var auth: FirebaseAuth) {

    val navigateToActivity = MutableLiveData<Class<*>>()

    val messageToast = MutableLiveData("")

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

    private fun obtainDataUserFromRealTimeDatabase(id: String) {
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

    private fun routeUserActivity(typeUser: String) {
        if (typeUser == "administrador"){
            navigateToActivity.value = MainAdminActivity::class.java
        }else{
            navigateToActivity.value = MainClientActivity::class.java
        }
    }

    private fun addCurrentUserToCompanionObject(user: User) {
        CurrentUser.currentUser = user
    }
}