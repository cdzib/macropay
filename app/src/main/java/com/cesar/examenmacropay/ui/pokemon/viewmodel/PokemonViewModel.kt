package com.cesar.examenmacropay.ui.pokemon.viewmodel

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cesar.examenmacropay.R
import com.cesar.examenmacropay.data.Result
import com.cesar.examenmacropay.data.model.JsonResponsePokemonDetail
import com.cesar.examenmacropay.data.model.Pokemon
import com.cesar.examenmacropay.data.repository.PokemonRepository
import com.cesar.examenmacropay.ui.pokemon.LoggedInPokemonView
import com.cesar.examenmacropay.ui.pokemon.PokemonFormState
import com.cesar.examenmacropay.ui.pokemon.PokemonResult


class PokemonViewModel(private val pokemonRepository: PokemonRepository) : ViewModel() {
    var offset = 0
    var isLoading = false
    var isLastPage = false
    var TOTAL_PAGES = 0
    val limit = 20
    var currentOffset = offset

    private val _pokemonForm = MutableLiveData<PokemonFormState>()
    val pokemonFormState: LiveData<PokemonFormState> = _pokemonForm

    private val _pokemonResult = MutableLiveData<PokemonResult>()
    val pokemonResult: LiveData<PokemonResult> = _pokemonResult

    private var _pokemonList = ArrayList<Pokemon>()

    suspend fun getPokemons(offset: Int, limit: Int) {
        val result = pokemonRepository.getPokemons(offset, limit)

        if (result is Result.Success) {
            result.data.count?.let {
                 TOTAL_PAGES = it
            }
            _pokemonList = result.data.results
            _pokemonResult.value =
                PokemonResult(success = LoggedInPokemonView(pokemons = _pokemonList))

        } else {
            _pokemonResult.value = PokemonResult(error = R.string.login_failed)
        }
    }

    suspend fun getPokemon(name: String): JsonResponsePokemonDetail {
        val result = pokemonRepository.getPokemon(name)

        return if (result is Result.Success) {
            result.data
        } else {
            getPokemon(name)
        }
    }
}