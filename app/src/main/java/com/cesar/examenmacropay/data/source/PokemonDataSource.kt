package com.cesar.examenmacropay.data.source

import android.widget.Toast
import com.cesar.examenmacropay.data.Result
import com.cesar.examenmacropay.data.model.JsonResponsePokemonDetail
import com.cesar.examenmacropay.data.model.JsonResponsePokemonList
import com.cesar.examenmacropay.data.model.LoggedInUser
import com.cesar.examenmacropay.data.remote.ApiPokemon
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.UUID


/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class PokemonDataSource(private val apiPokemon: ApiPokemon) {

    suspend fun getPokemons(offset: Int, limit: Int): Result<JsonResponsePokemonList>{
        try {
            return Result.Success( apiPokemon.apiService.getPokemons(offset,limit))
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    suspend fun getPokemon(name: String):  Result<JsonResponsePokemonDetail> {
        try {
            return Result.Success(apiPokemon.apiService.getPokemon(name))
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }
}