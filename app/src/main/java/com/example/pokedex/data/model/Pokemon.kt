package com.example.pokedex.data.model

data class Pokemon(
    val name: String,
    val url: String
)

data class PokemonDetail(
    val id: Int,
    val name: String,
    val sprites: Sprites,
    val types: List<PokemonType>
)

data class Sprites(
    val front_default: String
)

data class PokemonType(
    val type: Type
)

data class Type(
    val name: String
)