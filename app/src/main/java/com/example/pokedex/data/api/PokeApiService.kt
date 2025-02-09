package com.example.pokedex.data.api

import com.example.pokedex.viewmodel.Pokemon
import com.example.pokedex.viewmodel.PokemonDetail
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): PokemonListResponse

    @GET("pokemon/{name}")
    suspend fun getPokemonDetail(@Path("name") name: String): PokemonDetail
}

data class PokemonListResponse(
    val results: List<Pokemon>
)
