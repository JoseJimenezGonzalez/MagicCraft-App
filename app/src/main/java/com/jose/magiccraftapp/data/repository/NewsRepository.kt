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
import com.jose.magiccraftapp.data.model.Message
import com.jose.magiccraftapp.data.model.News

class NewsRepository (application: Application) {
    // Inicialización de DatabaseReference
    var dbRef = FirebaseDatabase.getInstance().reference

    // Inicialización de StorageReference
    var stRef = FirebaseStorage.getInstance().reference

    // Inicialización de FirebaseAuth
    var auth = FirebaseAuth.getInstance()

    fun getAllNews(): LiveData<MutableList<News>>{
        val messageLive = MutableLiveData<MutableList<News>>()
        dbRef.child("MagicCraft").child("News").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val newsList = mutableListOf<News>()
                    //Recorrer todos los menajes e ir añadiendolos a la lista
                    snapshot.children.forEach { snapshotBucle ->
                        val new = snapshotBucle.getValue(News::class.java)
                        if (new != null) {
                            newsList.add(new)
                        }
                    }
                    messageLive.value = newsList
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Error al obtener los mensajes: $error")
            }
        })
        return messageLive
    }
}