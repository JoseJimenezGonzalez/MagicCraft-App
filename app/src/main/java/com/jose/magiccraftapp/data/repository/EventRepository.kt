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
import com.jose.magiccraftapp.data.model.Event

class EventRepository(application: Application) {
    // Inicialización de DatabaseReference
    var dbRef = FirebaseDatabase.getInstance().reference

    // Inicialización de StorageReference
    var stRef = FirebaseStorage.getInstance().reference

    // Inicialización de FirebaseAuth
    var auth = FirebaseAuth.getInstance()

    fun getAllEvents(): LiveData<MutableList<Event>>{
        val mutableData = MutableLiveData<MutableList<Event>>()
        dbRef.child("MagicCraft").child("Events").addValueEventListener(object :
            ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val listEvents = mutableListOf<Event>()
                snapshot.children.forEach { eventSnapshot ->
                    val pojoEvent = eventSnapshot.getValue(Event::class.java)!!
                    listEvents.add(pojoEvent)
                }
                mutableData.value = listEvents
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        return mutableData
    }
}