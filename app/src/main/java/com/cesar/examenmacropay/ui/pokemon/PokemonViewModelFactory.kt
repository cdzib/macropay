package com.cesar.examenmacropay.ui.pokemon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cesar.examenmacropay.data.remote.ApiPokemon
import com.cesar.examenmacropay.data.source.LoginDataSource
import com.cesar.examenmacropay.data.repository.PokemonRepository
import com.cesar.examenmacropay.data.source.PokemonDataSource
import com.cesar.examenmacropay.ui.pokemon.viewmodel.PokemonViewModel

/**
 * ViewModel provider factory to instantiate PokemonViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class PokemonViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PokemonViewModel::class.java)) {
            return PokemonViewModel(
                pokemonRepository = PokemonRepository(
                    dataSource = PokemonDataSource(ApiPokemon())
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}