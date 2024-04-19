package com.jose.magiccraftapp.data.repository

import android.app.Application
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject

class MazoRepository @Inject constructor(private val dbRef: DatabaseReference, application: Application) {
}