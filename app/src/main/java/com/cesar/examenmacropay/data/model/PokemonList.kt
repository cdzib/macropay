package com.cesar.examenmacropay.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data  class JsonResponsePokemonList(
        @SerializedName("count"    ) var count    : Int?               = null,
        @SerializedName("next"     ) var next     : String?            = null,
        @SerializedName("previous" ) var previous : String?            = null,
        @SerializedName("results"  ) var results  : ArrayList<Pokemon> = arrayListOf()
)
data class Pokemon (
    @SerializedName("name" ) var name : String? = null,
    @SerializedName("url"  ) var url  : String? = null,
    var detail: JsonResponsePokemonDetail? = null
)
data class JsonResponsePokemonDetail (

    @SerializedName("abilities"                ) var abilities              : ArrayList<Abilities>   = arrayListOf(),
    @SerializedName("base_experience"          ) var baseExperience         : Int?                   = null,
    //@SerializedName("cries"                    ) var cries                  : Cries?                 = Cries(),
    @SerializedName("forms"                    ) var forms                  : ArrayList<Forms>       = arrayListOf(),
    //@SerializedName("game_indices"             ) var gameIndices            : ArrayList<GameIndices> = arrayListOf(),
    @SerializedName("height"                   ) var height                 : Int?                   = null,
    //@SerializedName("held_items"               ) var heldItems              : ArrayList<String>      = arrayListOf(),
    @SerializedName("id"                       ) var id                     : Int?                   = null,
    //@SerializedName("is_default"               ) var isDefault              : Boolean?               = null,
    //@SerializedName("location_area_encounters" ) var locationAreaEncounters : String?                = null,
    //@SerializedName("moves"                    ) var moves                  : ArrayList<Moves>       = arrayListOf(),
    @SerializedName("name"                     ) var name                   : String?                = null,
    //@SerializedName("order"                    ) var order                  : Int?                   = null,
    //@SerializedName("past_abilities"           ) var pastAbilities          : ArrayList<String>      = arrayListOf(),
    //@SerializedName("past_types"               ) var pastTypes              : ArrayList<String>      = arrayListOf(),
    //@SerializedName("species"                  ) var species                : Species?               = Species(),*/
    @SerializedName("sprites"                  ) var sprites                : Sprites?               = Sprites(),
    @SerializedName("stats"                    ) var stats                  : ArrayList<Stats>       = arrayListOf(),
    @SerializedName("types"                    ) var types                  : ArrayList<Types>       = arrayListOf(),
    @SerializedName("weight"                   ) var weight                 : Int?                   = null

)
data class Abilities (
    @SerializedName("ability"   ) var ability  : Ability? = Ability(),
    @SerializedName("is_hidden" ) var isHidden : Boolean? = null,
    @SerializedName("slot"      ) var slot     : Int?     = null
)

data class Ability (
    @SerializedName("name" ) var name : String? = null,
    @SerializedName("url"  ) var url  : String? = null
)

data class Sprites (
    @SerializedName("front_default"      ) var frontDefault     : String? = null,
)

data class Forms (
    @SerializedName("name" ) var name : String? = null,
    @SerializedName("url"  ) var url  : String? = null
)
data class Types (
    @SerializedName("slot" ) var slot : Int?  = null,
    @SerializedName("type" ) var type : Type? = Type()
)
data class Type (
    @SerializedName("name" ) var name : String? = null,
    @SerializedName("url"  ) var url  : String? = null
)

data class Stats (
    @SerializedName("base_stat" ) var baseStat : Int?  = null,
    @SerializedName("effort"    ) var effort   : Int?  = null,
    @SerializedName("stat"      ) var stat     : Stat? = Stat()
)

data class Stat (
    @SerializedName("name" ) var name : String? = null,
    @SerializedName("url"  ) var url  : String? = null
)