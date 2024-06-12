package com.cesar.examenmacropay.ui.pokemon

import com.cesar.examenmacropay.ui.login.LoggedInUserView

class PokemonResult(
    val success: LoggedInPokemonView? = null,
    val error: Int? = null
)