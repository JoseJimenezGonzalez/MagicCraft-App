package com.jose.magiccraftapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.jose.magiccraftapp.admin.MainAdminActivity
import com.jose.magiccraftapp.client.MainClientActivity
import com.jose.magiccraftapp.databinding.ActivityLoginBinding
import com.jose.magiccraftapp.model.CurrentUser
import com.jose.magiccraftapp.model.User
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    @Inject
    lateinit var dbRef: DatabaseReference

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Codigo

        actionButtonGoToRegister()

        actionButtonLogin()
    }

    private fun actionButtonLogin() {
        binding.btnIniciarSesion.setOnClickListener {
            val mail = binding.tietCorreo.text.toString().trim()
            val password = binding.tietPassword.text.toString().trim()
            loginAuth(mail, password)
        }
    }

    private fun loginAuth(mail: String, password: String) {
        auth.signInWithEmailAndPassword(mail, password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                val userAuth = auth.currentUser
                val id = userAuth!!.uid
                obtainDataUserFromRealTimeDatabase(id)
            }else{
                generateToast("No existe ese usuario en la base de datos")
            }
        }
    }

    private fun obtainDataUserFromRealTimeDatabase(id: String) {
        dbRef.child("MagicCraft").child("Users").child(id).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    //Recojo toda la informacion del usuario
                    val user = snapshot.getValue(User::class.java)
                    val typeUser = user!!.typeUser
                    addCurrentUserToCompanionObject(user)
                    goToMainUserActivity(typeUser)
                }else{
                    println("El usuario con ID $id no existe")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Error al obtener datos del usuario: $error")
            }

        })
    }

    private fun goToMainUserActivity(typeUser: String) {
        if (typeUser == "administrador"){
            val intent = Intent(this, MainAdminActivity::class.java)
            startActivity(intent)
        }else{
            val intent = Intent(this, MainClientActivity::class.java)
            startActivity(intent)
        }
    }

    private fun addCurrentUserToCompanionObject(user: User) {
        CurrentUser.currentUser = user
    }

    private fun actionButtonGoToRegister() {
        binding.tvCrearCuenta.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun generateToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}