package com.example.pokedex.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.data.model.Pokemon
import com.example.pokedex.data.model.PokemonDetail
import com.example.pokedex.data.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _pokemonList = mutableStateListOf<Pokemon>()
    val pokemonList: List<Pokemon> get() = _pokemonList

    private val _selectedPokemon = mutableStateOf<PokemonDetail?>(null)

    fun loadPokemonList(limit: Int, offset: Int) {
        viewModelScope.launch{
            try {
                val result = repository.getPokemonList(limit, offset)
                _pokemonList.clear()
                _pokemonList.addAll(result)
            } catch (e: Exception) {
                // tratamento do erro pesando em algo... espero não esquecer de mexer aqui
            }
        }
    }

    fun loadPokemonDetail(name: String) {
        viewModelScope.launch {
            try {
                _selectedPokemon.value = repository.getPokemonDetail(name)
            } catch (e: Exception) {
                // tratamento do erro pesando em algo... espero não esquecer de mexer aqui
            }
        }
    }
}

