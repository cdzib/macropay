package com.cesar.examenmacropay.ui.pokemon

import com.cesar.examenmacropay.data.model.Pokemon

/**
 * User details post authentication that is exposed to the UI
 */
data class LoggedInPokemonView(
    val pokemons: ArrayList<Pokemon>
)