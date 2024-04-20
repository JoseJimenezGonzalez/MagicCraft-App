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

class MessageRepository(application: Application) {
    // Inicialización de DatabaseReference
    var dbRef = FirebaseDatabase.getInstance().reference

    // Inicialización de StorageReference
    var stRef = FirebaseStorage.getInstance().reference

    // Inicialización de FirebaseAuth
    var auth = FirebaseAuth.getInstance()

    fun getMessages(idChat: String) : LiveData<MutableList<Message>> {
        val messageLive = MutableLiveData<MutableList<Message>>()
        dbRef.child("MagicCraft").child("Chat").child(idChat).addValueEventListener(object :
            ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val messagesList = mutableListOf<Message>()
                    //Recorrer todos los menajes e ir añadiendolos a la lista
                    snapshot.children.forEach { snapshotBucle ->
                        val message = snapshotBucle.getValue(Message::class.java)
                        if (message != null) {
                            messagesList.add(message)
                        }
                    }
                    messageLive.value = messagesList
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Error al obtener los mensajes: $error")
            }
        })
        return messageLive
    }
}