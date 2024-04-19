package com.jose.magiccraftapp.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject

class MazoViewModel @Inject constructor(private val dbRef: DatabaseReference, application: Application): AndroidViewModel(application) {
}