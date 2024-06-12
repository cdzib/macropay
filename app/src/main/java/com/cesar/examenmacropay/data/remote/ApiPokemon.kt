package com.cesar.examenmacropay.data.remote
import com.cesar.examenmacropay.data.model.JsonResponsePokemonDetail
import com.cesar.examenmacropay.data.model.JsonResponsePokemonList
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServicePokemon {
    @GET("pokemon")
    suspend fun getPokemons(@Query("offset") offset: Int, @Query("limit") limit: Int): JsonResponsePokemonList

    @GET("pokemon/{id}")
    suspend fun getPokemon(@Path("id") name: String): JsonResponsePokemonDetail
}

class ApiPokemon {
    val apiService: ApiServicePokemon by lazy {
        RetrofitClient.retrofit.create(ApiServicePokemon::class.java)
    }
}

object RetrofitClient {
    private const val BASE_URL = "https://pokeapi.co/api/v2/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}