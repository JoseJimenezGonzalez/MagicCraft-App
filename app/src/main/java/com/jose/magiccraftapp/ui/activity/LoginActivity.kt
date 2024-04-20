package com.jose.magiccraftapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.jose.magiccraftapp.R
import com.jose.magiccraftapp.data.entity.Carta
import com.jose.magiccraftapp.data.entity.Mazo
import com.jose.magiccraftapp.data.model.Card
import com.jose.magiccraftapp.data.model.CurrentUser
import com.jose.magiccraftapp.data.model.Deck
import com.jose.magiccraftapp.data.repository.CartaRepository
import com.jose.magiccraftapp.data.repository.MazoRepository
import com.jose.magiccraftapp.data.repository.UsuarioRepository
import com.jose.magiccraftapp.data.viewmodel.UsuarioViewModel
import com.jose.magiccraftapp.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    @Inject
    lateinit var dbRef: DatabaseReference

    @Inject
    lateinit var auth: FirebaseAuth

    private val userViewModel: UsuarioViewModel by viewModels()

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
            binding.progressBar.visibility = View.VISIBLE
            val mail = binding.tietCorreo.text.toString().trim()
            val password = binding.tietPassword.text.toString().trim()
            userViewModel.obtainUser(mail, password).observe(this, Observer {  usuario ->
                if(usuario == null){
                    generateToast("Usuario incorrecto")
                }else{
                    generateToast("Usuario correcto")
                    //Guardar usuario en componion
                    CurrentUser.currentUser = usuario
                    //Guardar usuario en la base de datos
                    val usuarioRepository = UsuarioRepository(application)
                    CoroutineScope(Dispatchers.IO).launch {
                        usuarioRepository.insert(usuario)
                    }
                    //Guardar usuario en shared
                    //Navegar
                    if(usuario.typeUser == "administrador"){
                        val intent = Intent(this, MainAdminActivity::class.java)
                        startActivity(intent)
                    }else{
                        //Inserto mazos y cartas
                        dbRef.child("MagicCraft").child("Decks").child(usuario.idUsuario).addValueEventListener(object :
                            ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val deckList = mutableListOf<Deck>()
                                snapshot.children.forEach { deckSnapshot ->
                                    val pojoDeck = deckSnapshot.getValue(Deck::class.java)!!
                                    // Recuperar las cartas de cada mazo
                                    deckSnapshot.child("Cards").children.forEach { cardSnapshot ->
                                        val pojoCard = cardSnapshot.getValue(Card::class.java)!!
                                        pojoDeck.cards.add(pojoCard)
                                    }
                                    deckList.add(pojoDeck)
                                }
                                //Transformo la lista
                                val listaMazo = deckList.map { deck ->
                                    Mazo(
                                        deck.idDeck,
                                        deck.idUserDeck,
                                        deck.nameDeck,
                                        deck.formatDeck,
                                        "combo",
                                        deck.urlImageFirebase
                                    )
                                }.toMutableList()
                                val listaCartas = deckList.flatMap { deck ->
                                    deck.cards.map { card ->
                                        Carta(
                                            card.id,
                                            deck.idDeck,
                                            card.cmc,
                                            card.numberCard,
                                            card.text,
                                            card.type,
                                            "blanco",
                                            card.urlArtCrop,
                                            card.urlArtNormal
                                        )
                                    }
                                }.toMutableList()
                                CoroutineScope(Dispatchers.IO).launch {
                                    val mazoRepository = MazoRepository(application)
                                    val cartaRepository = CartaRepository(application)
                                    mazoRepository.insertAll(listaMazo)
                                    cartaRepository.insertAll(listaCartas)
                                }

                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Handle error
                            }
                        })
                        val intent = Intent(this, MainClientActivity::class.java)
                        startActivity(intent)
                    }
                }
            })
        }
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