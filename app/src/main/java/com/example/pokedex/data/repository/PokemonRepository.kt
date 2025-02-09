package com.example.pokedex.data.repository

import com.example.pokedex.data.api.PokeApiService
import com.example.pokedex.viewmodel.Pokemon
import com.example.pokedex.viewmodel.PokemonDetail
import javax.inject.Inject

interface PokemonRepository {
    suspend fun getPokemonList(limit: Int, offset: Int): List<Pokemon>
    suspend fun getPokemonDetail(name: String): PokemonDetail
}

class PokemonRepositoryImplementation @Inject constructor(
    private val api: PokeApiService
) : PokemonRepository {

    override suspend fun getPokemonList(limit: Int, offset: Int): List<Pokemon> =
        api.getPokemonList(limit, offset).results

    override suspend fun getPokemonDetail(name: String): PokemonDetail =
        api.getPokemonDetail(name)
}