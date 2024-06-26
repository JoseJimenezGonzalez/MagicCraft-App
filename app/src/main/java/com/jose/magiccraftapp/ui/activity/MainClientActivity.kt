package com.jose.magiccraftapp.ui.activity

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.provider.Settings
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.NotificationCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.data.model.Event
import com.jose.magiccraftapp.databinding.ActivityMainClientBinding
import com.jose.magiccraftapp.util.addEventIdToSharedPreferences
import com.jose.magiccraftapp.util.getBooleanPreference
import com.jose.magiccraftapp.util.getEventIdsFromSharedPreferences
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

@AndroidEntryPoint
class MainClientActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainClientBinding

    private lateinit var navController: NavController

    private lateinit var androidId: String

    private lateinit var generador: AtomicInteger

    @Inject
    lateinit var dbRef: DatabaseReference

    @Inject
    lateinit var stRef: StorageReference

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        binding = ActivityMainClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        comprobarTheme()
        initUI()
        crearCanalNotificaciones()
        androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        generador = AtomicInteger(0)

        //Controlar notificaciones
        dbRef.child("MagicCraft").child("Events").addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val pojoEvento = snapshot.getValue(Event::class.java)
                if(pojoEvento != null){
                    // Comprobar si ya se ha generado una notificación para este evento
                    val setId = getEventIdsFromSharedPreferences()
                    Log.e("ListaSetId", setId.toString())
                    if (!setId.contains(pojoEvento.id)) {
                        // Generar la notificación
                        generarNotificacion(
                            generador.incrementAndGet(),
                            pojoEvento,
                            "Nuevo evento " + pojoEvento.nombre + " de " + pojoEvento.formato + " con fecha " + pojoEvento.fecha + ".",
                            pojoEvento.nombre,
                            MainClientActivity::class.java
                        )
                        // Añadir el ID del evento a las preferencias compartidas
                        addEventIdToSharedPreferences(pojoEvento.id)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                //Nada
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                //Nada
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                //Nada
            }

            override fun onCancelled(error: DatabaseError) {
                //Nada
            }

        })


    }

    private fun initUI() {
        initNavigation()
    }

    private fun initNavigation() {
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_client) as NavHostFragment
        navController = navHost.navController
        binding.bottomNavigationViewClient.setupWithNavController(navController)
    }

    @SuppressLint("ServiceCast")
    private fun generarNotificacion(
        id_noti: Int,
        pojo: Parcelable,
        contenido: String,
        titulo: String,
        destino: Class<*>
    ) {
        val id = "canal_cliente"
        val actividad = Intent(applicationContext, destino).apply{
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK //Para evitar bugs en la aplicacion
        }

        actividad.putExtra("event", pojo)

        val pendingIntent =
            PendingIntent.getActivity(this, 0, actividad, PendingIntent.FLAG_IMMUTABLE)

        val notificacion = NotificationCompat.Builder(this, id)
            .setSmallIcon(R.drawable.ic_eventt)
            .setContentTitle(titulo)
            .setContentText(contenido)
            .setSubText("Sistema de notificación")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()


        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(id_noti, notificacion)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun crearCanalNotificaciones() {
        val nombre = "canal_basico"
        val id = "canal_cliente"
        val descripcion = "Notificacion basica"
        val importancia = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(id, nombre, importancia).apply {
            description = descripcion
        }

        val nm: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.createNotificationChannel(channel)
    }

    private fun comprobarTheme() {
        val modoDia = this.getBooleanPreference("modo_dia")
        if(modoDia){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }


}