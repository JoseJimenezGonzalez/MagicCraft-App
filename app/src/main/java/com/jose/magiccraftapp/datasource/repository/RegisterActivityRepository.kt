package com.jose.magiccraftapp.datasource.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.jose.magiccraftapp.datasource.model.User
import com.jose.magiccraftapp.datasource.model.UserForm
import javax.inject.Inject

class RegisterActivityRepository @Inject constructor(private val dbRef: DatabaseReference, var auth: FirebaseAuth) {

    val toastMessage = MutableLiveData("")

    fun registerUserAuth(userForm: UserForm) {
        val mail = userForm.mail
        val password = userForm.password
        auth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                val userAuth = auth.currentUser
                //Lamada a funcion para meter los datos en la RealTimeDatabase
                registerUserRealTimeDatabase(userForm, userAuth!!)
            }else{
                toastMessage.value = "Ya existe una cuenta con ese correo en nuestra base de datos"
            }
        }
    }

    private fun registerUserRealTimeDatabase(userForm: UserForm, userAuth: FirebaseUser) {
        val id = userAuth.uid
        val isUserAdmin = isAdministratorUser(userForm.mail)
        val typeUser = typeOfUser(isUserAdmin)
        dbRef.child("MagicCraft").child("Users").child(id).setValue(
            User(
                id,
                userForm.name,
                userForm.surname,
                userForm.mail,
                userForm.password,
                typeUser
            )
        )
        toastMessage.value = "Se ha registrado correctamente"
    }

    private fun isAdministratorUser(email: String): Boolean = email=="administrador@gmail.com"

    private fun typeOfUser(bol: Boolean): String = if (bol) "administrador" else "cliente"
}