package com.example.pokedex.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.data.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ApiUiState {
    LOADING, SUCCESS
}

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

data class PokemonDetail(
    val id: Int,
    val name: String,
    val sprites: Sprites?,
    val types: List<PokemonType>
)

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
    val limit: Int = 20,
    val pokemonList: List<Pokemon> = emptyList()
)

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _apiUiState = mutableStateOf(ApiUiState.LOADING)

    private val _pokemonList = mutableStateListOf<Pokemon>()

    private val _showDetail = mutableStateOf(ShowDetail())
    val showDetail: ShowDetail get() = _showDetail.value

    private val _selectedPokemon = mutableStateOf<PokemonDetail?>(null)
    val selectedPokemon: PokemonDetail? get() = _selectedPokemon.value

    private var searchQuery by mutableStateOf("")

    init {
        loadPokemonList(limit = _showDetail.value.limit)
    }

    // Função genérica para mudar o estado do carregamento
    private fun setLoadingState(state: ApiUiState) {
        _apiUiState.value = state
    }

    // Função para carregar lista de pokémons
    private fun loadPokemonList() {
        setLoadingState(ApiUiState.LOADING)
        viewModelScope.launch {
            try {
                val result = repository.getPokemonList(1025, 0)
                _pokemonList.clear()
                _pokemonList.addAll(result)
                setLoadingState(ApiUiState.SUCCESS)
            } catch (e: Exception) {
                setLoadingState(ApiUiState.LOADING)
            }
        }
    }

    // Função para carregar detalhes de um Pokémon
    fun loadPokemonDetail(name: String, id: Int) {
        setLoadingState(ApiUiState.LOADING)
        viewModelScope.launch {
            try {
                _selectedPokemon.value = repository.getPokemonDetail(name)
                setLoadingState(ApiUiState.SUCCESS)
                _showDetail.value = ShowDetail(show = true, pokemonIndex = id)
            } catch (e: Exception) {
                setLoadingState(ApiUiState.LOADING)
            }
        }
    }

    fun getFilteredPokemonList(): List<Pokemon> {
        return if (searchQuery.isBlank()) {
            _pokemonList
        } else {
            _pokemonList.filter { it.name.contains(searchQuery, ignoreCase = true) }
        }
    }

    fun updateSearchQuery(query: String) {
        searchQuery = query
    }

    fun hideDetail() {
        _showDetail.value = ShowDetail(show = false)
    }

    fun nextPokemonDetail() {
        if (showDetail.pokemonIndex < _pokemonList.size - 1) {
            loadPokemonDetail(
                _pokemonList[showDetail.pokemonIndex + 1].name, showDetail.pokemonIndex + 1
            )
        }
    }

    fun previousPokemonDetail() {
        if (showDetail.pokemonIndex > 1) {
            loadPokemonDetail(
                _pokemonList[showDetail.pokemonIndex - 1].name, showDetail.pokemonIndex - 1
            )
        }
    }
}
