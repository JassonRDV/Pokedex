package com.example.pokedex.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.data.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PokemonDetail(
    val id: Int,
    val name: String,
    val sprites: Sprites?,
    val types: List<PokemonType>
)

data class Pokemon(
    val name: String,
    val url: String,
) {
    val imageUrl: String
        get() {
            val id = url.trimEnd('/').substringAfterLast("/")
            return if (id.toIntOrNull() != null) {
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
            } else {
                ""
            }
        }
}

data class Sprites(
    val front_default: String?
)

data class PokemonType(
    val type: Type
)

data class Type(
    val name: String
)

data class ShowDetail(
    val show: Boolean = false,
    val pokemonIndex: Int = 1,
    val limit: Int = 44,
)

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _pokemonList = mutableStateListOf<Pokemon>()
    val pokemonList: List<Pokemon> get() = _pokemonList

    private val _showDetail = mutableStateOf(ShowDetail())
    val showDetail: ShowDetail get() = _showDetail.value

    private val _selectedPokemon = mutableStateOf<PokemonDetail?>(null)
    val selectedPokemon: PokemonDetail? get() = _selectedPokemon.value

    fun loadPokemonList(limit: Int, offset: Int) {
        viewModelScope.launch {
            try {
                val result = repository.getPokemonList(limit, offset)
                _pokemonList.clear()
                _pokemonList.addAll(result)
            } catch (e: Exception) {
                // tratamento do erro pesando em algo... espero não esquecer de mexer aqui
            }
        }
    }

    fun loadPokemonDetail(name: String, id: Int) {
        viewModelScope.launch {
            try {
                _selectedPokemon.value = repository.getPokemonDetail(name)
            } catch (e: Exception) {
                // tratamento do erro pesando em algo... espero não esquecer de mexer aqui
            }
            _showDetail.value = ShowDetail(show = true, pokemonIndex = id)
        }
    }

    fun hideDetail() {
        _showDetail.value = ShowDetail(false)
    }

    fun nextPokemonDetail() {
        if (showDetail.pokemonIndex < pokemonList.size) {
            loadPokemonDetail(pokemonList[showDetail.pokemonIndex + 1].name, showDetail.pokemonIndex + 1)
        }
    }

    fun previousPokemonDetail() {
        if (showDetail.pokemonIndex > 1) {
            loadPokemonDetail(pokemonList[showDetail.pokemonIndex - 1].name, showDetail.pokemonIndex - 1)
        }
    }

    fun loadMore() {
        _showDetail.value = ShowDetail(limit = _showDetail.value.limit + 44)
        loadPokemonList(_showDetail.value.limit, 0)
    }
}

