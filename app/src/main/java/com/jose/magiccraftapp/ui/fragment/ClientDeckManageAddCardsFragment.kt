package com.jose.magiccraftapp.ui.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.jose.magiccraftapp.databinding.FragmentClientDeckManageAddCardsBinding
import com.jose.magiccraftapp.datasource.dataclass.CardDataclass
import com.jose.magiccraftapp.data.model.Card
import com.jose.magiccraftapp.data.model.CurrentUser
import com.jose.magiccraftapp.datasource.retrofit.ApiRequest
import com.jose.magiccraftapp.datasource.retrofit.RetrofitService
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ClientDeckManageAddCardsFragment : Fragment() {

    private var _binding: FragmentClientDeckManageAddCardsBinding? = null
    private val binding get() = _binding!!

    private lateinit var currentCard: CardDataclass

    val apiRequest = object: ApiRequest{}

    val retrofit = apiRequest.retrofit()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientDeckManageAddCardsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Codigo
        setUpSearchBar()
        setUpButtonAddCard()
    }

    private fun setUpButtonAddCard() {
        binding.btnAddCard.setOnClickListener {
            val id = currentCard.id
            val name = currentCard.name
            val type = currentCard.type_line
            val text = currentCard.oracle_text
            val urlArtCrop = currentCard.image_uris.art_crop
            val urlArtNormal = currentCard.image_uris.normal
            val numberOfCard = 4
            val cmc = currentCard.cmc

            //Metemos la carta en current card
            CurrentUser.currentCard = Card(
                id, name, type, text, urlArtCrop, urlArtNormal, 4, cmc
            )
            //Ahora añadimos la carta al mazo actual
            CurrentUser.currentDeck!!.cards.add(CurrentUser.currentCard!!)
            Log.e("Carta introducida", "${CurrentUser.currentCard!!.name}")
            Log.e("Tamaño del mazo", "${CurrentUser.currentDeck!!.nameDeck} y tiene ${CurrentUser.currentDeck!!.cards.size} cartas")
            Log.e("Nombre de la carta del mazo", "${CurrentUser.currentDeck!!.cards.toString()}")
        }
    }

    private fun setUpSearchBar() {
        binding.svCard.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Este método se llama cuando se envía la consulta.
                // Aquí puedes procesar la consulta.
                // Por ejemplo, puedes buscar la consulta en tu base de datos.
                binding.ly.visibility = View.INVISIBLE
                searchCardApi(query!!)
                hideKeyboard()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Este método se llama cuando cambia el texto de la consulta.
                // Aquí puedes procesar el nuevo texto.
                // Por ejemplo, puedes mostrar sugerencias basadas en el nuevo texto.
                return true
            }
        })
    }

    private fun searchCardApi(cardName: String) {
        val service = retrofit.create(RetrofitService::class.java)
        //5. Corrutina
        lifecycleScope.launch {
            try {
                val response = service.getCardByName(cardName)
                val imageUrl = response.image_uris.normal
                Glide.with(requireContext()).load(imageUrl).into(binding.ivCard)
                currentCard = response
                binding.ly.visibility = View.VISIBLE
            } catch (e: HttpException) {
                // La solicitud no tuvo éxito debido a un error HTTP
                Toast.makeText(context, "La carta no fue encontrada", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                // Error de red
                Toast.makeText(context, "Error de red", Toast.LENGTH_SHORT).show()
            } catch (e: Throwable) {
                // Otro tipo de error
                Toast.makeText(context, "Error inesperado", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}