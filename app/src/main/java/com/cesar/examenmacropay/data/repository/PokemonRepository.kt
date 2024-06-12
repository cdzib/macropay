package com.cesar.examenmacropay.data.repository

import com.cesar.examenmacropay.data.Result
import com.cesar.examenmacropay.data.model.JsonResponsePokemonDetail
import com.cesar.examenmacropay.data.model.JsonResponsePokemonList
import com.cesar.examenmacropay.data.model.Pokemon
import com.cesar.examenmacropay.data.source.PokemonDataSource

class PokemonRepository(val dataSource: PokemonDataSource) {
    // in-memory cache of the loggedInUser object
    var _pokemonList: ArrayList<Pokemon>? = null
        private set

    val pokemonList: Boolean
        get() = _pokemonList != null

    init {
        _pokemonList = null
    }
    suspend fun getPokemons(offset: Int, limit: Int): Result<JsonResponsePokemonList> {

        val result = dataSource.getPokemons(offset,limit)

        if (result is Result.Success) {
            setPokemonList(result.data.results)
        }

        return result
    }

    suspend fun getPokemon(name: String):  Result<JsonResponsePokemonDetail> {
        return dataSource.getPokemon(name)
    }


    private fun setPokemonList(pokemonList: ArrayList<Pokemon>) {
        this._pokemonList = pokemonList
    }
}