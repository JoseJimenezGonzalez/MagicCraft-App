package com.jose.magiccraftapp.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import java.text.SimpleDateFormat

//Fecha
fun Long.format(format: String = "dd/MM/yyyy"): String {
    return SimpleDateFormat(format).format(this)
}
//Obtener el id del dispositivo
@SuppressLint("MissingPermission", "HardwareIds")
fun Context.getDeviceIdExtensions(): String {
    var android_id: String? = ""
    try {
        android_id = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)

        val tm: TelephonyManager? = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
        if (android_id?.isEmpty() ?: false) {
            android_id = tm?.getDeviceId()
        }
        if (android_id?.isEmpty() ?: false) {
            android_id = tm?.subscriberId
        }
    } catch (e: Exception) {
        Log.e("GetDeviceId", e.message.toString())
    }

    return android_id ?: ""
}

//¿Tiene internet el dispositivo?
fun isInternetAvailable(context: Context): Boolean {
    var result = false
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities = connectivityManager.activeNetwork ?: return false
    val actNw =
        connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
    result = when {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true//Wifi
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true//3g, 4g o 5g
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true//cable
        else -> false
    }

    return result
}
//Shared preferences
fun Context.getPreferences(): SharedPreferences {
    return this.getSharedPreferences("AGPreferences", 0)
}

fun Context.clearPreferences() {
    getPreferences().edit().clear().apply()
}

fun Context.putPreference(key: String, value: Boolean) {
    getPreferences().edit().putBoolean(key, value).apply()
}

fun Context.putPreference(key: String, value: String) {
    getPreferences().edit().putString(key, value).apply()
}

fun Context.putPreference(key: String, value: Int) {
    getPreferences().edit().putInt(key, value).apply()
}

fun Context.putPreference(key: String, value: Float) {
    getPreferences().edit().putFloat(key, value).apply()
}

fun Context.putPreference(key: String, value: Double) {
    val doubleString: String = value.toString()
    getPreferences().edit().putString(key, doubleString).apply()
}

fun Context.putPreference(key: String, value: Long) {
    getPreferences().edit().putLong(key, value).apply()
}

fun Context.getBooleanPreference(key: String): Boolean {
    return getPreferences().getBoolean(key, false)
}

fun Context.getDoublePreference(key: String): Double {
    val value = getPreferences().getString(key, "0")
    if (value != null) {
        return value.toDouble()
    }else{
        return 0.0
    }
}

fun Context.getStringPreference(key: String): String {
    return getPreferences().getString(key, "").toString()
}

fun Context.getIntPreference(key: String, defaultValue: Int): Int {
    return getPreferences().getInt(key, defaultValue)
}

fun Context.getFloatPreference(key: String, defaultValue: Float): Float {
    return getPreferences().getFloat(key, defaultValue)
}

fun Context.getIntPreference(key: String): Int {
    return getPreferences().getInt(key, 0)
}

fun Context.getLongPreference(key: String): Long {
    return getPreferences().getLong(key, 0L)
}

fun Context.removePreference(key: String) {
    return getPreferences().edit().remove(key).apply()
}



// Guardar un ID de evento en las preferencias compartidas
fun Context.addEventIdToSharedPreferences(eventId: String) {
    val eventSet = getPreferences().getStringSet("event_ids", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
    if(!eventSet.contains(eventId)){
        eventSet.add(eventId)
        with(getPreferences().edit()) {
            putStringSet("event_ids", eventSet)
            apply()
        }
    }
}

// Función para limpiar event_ids en las preferencias compartidas
fun Context.clearEventIdsFromSharedPreferences() {
    with(getPreferences().edit()) {
        remove("event_ids")
        apply()
    }
}

// Obtener todos los IDs de eventos de las preferencias compartidas
fun Context.getEventIdsFromSharedPreferences(): Set<String> {
    return getPreferences().getStringSet("event_ids", mutableSetOf())?.toSet() ?: setOf()
}

fun <T> List<T>.chunked(size: Int): List<List<T>> {
    val chunked = mutableListOf<List<T>>()
    for (i in indices step size) {
        val end = (i + size).coerceAtMost(this.size)
        chunked.add(subList(i, end))
    }
    return chunked
}