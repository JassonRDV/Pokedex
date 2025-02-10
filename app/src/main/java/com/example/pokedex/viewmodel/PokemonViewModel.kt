package com.example.pokedex.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.data.repository.PokemonRepository
import com.google.gson.annotations.SerializedName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ApiUiState {
    LOADING, SUCCESS, ERROR
}

data class Pokemon(
    val name: String,
    val url: String,
) {
    val id: Int
        get() = url.trimEnd('/').substringAfterLast("/").toIntOrNull() ?: -1

    val imageUrl: String
        get() = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
}

data class PokemonDetail(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<TypeWrapper>,
    val sprites: Sprites
)

data class TypeWrapper(
    val type: Type
)

data class Type(
    val name: String
)

data class Sprites(
    @SerializedName("front_default") val frontDefault: String
)

data class UiState(
    val show: Boolean = false,
    val pokemonIndex: Int = 1,
    val limit: Int = 20,
    val pokemonList: List<Pokemon> = emptyList(),
    val pokemonQuery: List<Pokemon> = emptyList(),
    val selectedPokemon: PokemonDetail? = null,
    val searchQuery: String = "",
    val apiUiStateGrid: ApiUiState = ApiUiState.LOADING,
    val apiUiStateDetail: ApiUiState = ApiUiState.LOADING
)

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadPokemonList()
    }

    private fun loadPokemonList() {
        viewModelScope.launch {
            try {
                val result = repository.getPokemonList(1025, 0)
                _uiState.value = _uiState.value.copy(
                    pokemonList = result,
                    pokemonQuery = result,
                    apiUiStateGrid = ApiUiState.SUCCESS
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    apiUiStateGrid = ApiUiState.ERROR
                )
            }
        }
    }

    fun loadPokemonDetail(name: String, id: Int) {
        viewModelScope.launch {
            try {
                val result = repository.getPokemonDetail(name)
                _uiState.value = _uiState.value.copy(
                    show = true,
                    pokemonIndex = id,
                    selectedPokemon = result,
                    apiUiStateDetail = ApiUiState.SUCCESS
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    apiUiStateDetail = ApiUiState.ERROR
                )
            }
            Log.d("PokemonViewModel", "loadPokemonDetail: ${_uiState.value.pokemonList}")
        }
    }

    fun getFilteredPokemonList() {
        val query = _uiState.value.searchQuery
        if (query.isBlank()) {
            val result = _uiState.value.pokemonList
            _uiState.update {
                it.copy(pokemonQuery = result)
            }
        } else {
            val result = _uiState.value.pokemonList.filter {
                it.name.contains(query, ignoreCase = true)
            }
            _uiState.update {
                it.copy(pokemonQuery = result)
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun hideDetail() {
        _uiState.value = _uiState.value.copy(
            show = false,
            selectedPokemon = null
        )
    }

    fun nextPokemonDetail() {
        val currentIndex = _uiState.value.pokemonIndex
        if (currentIndex < _uiState.value.pokemonQuery.size - 1) {
            val nextPokemon = _uiState.value.pokemonQuery.getOrNull(currentIndex + 1)
            nextPokemon?.let {
                loadPokemonDetail(it.name, currentIndex + 1)
            }
        }
    }

    fun previousPokemonDetail() {
        val currentIndex = _uiState.value.pokemonIndex
        if (currentIndex > 1) {
            val previousPokemon = _uiState.value.pokemonQuery.getOrNull(currentIndex - 1)
            previousPokemon?.let {
                loadPokemonDetail(it.name, currentIndex - 1)
            }
        }
    }
}
