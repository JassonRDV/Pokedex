package com.example.pokedex.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.pokedex.viewmodel.Pokemon
import com.example.pokedex.viewmodel.PokemonViewModel
import com.example.pokedex.viewmodel.UiState

@Composable
fun PokemonListScreen(
    modifier: Modifier,
    viewModel: PokemonViewModel,
    uiState: UiState,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = modifier
            .padding(4.dp)
            .fillMaxSize()
    ) {
        items(uiState.pokemonQuery.size) { index ->
            val pokemon = uiState.pokemonQuery[index]
            val pokeId = pokemon.id
            PokemonItem(
                pokemon = pokemon,
                viewModel = viewModel,
                uiState = uiState,
                index = index,
                pokeId = pokeId
            )
        }
    }
}

@Composable
private fun PokemonItem(
    pokemon: Pokemon,
    viewModel: PokemonViewModel,
    uiState: UiState,
    index: Int,
    pokeId: Int
) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.inversePrimary,
            contentColor = MaterialTheme.colorScheme.scrim
        ),
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .then(
                if (!uiState.show) {
                    Modifier.clickable(
                        onClick = {
                            viewModel.loadPokemonDetail((index + 1).toString(), index)
                        }
                    )
                } else {
                    Modifier
                }
            )
    ) {
        AsyncImage(
            model = pokemon.imageUrl,
            contentDescription = pokemon.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
        )
        {
            Text(
                text = String.format("N° %04d", pokeId),
                maxLines = 1,
                fontSize = 8.sp,
                modifier = Modifier
            )
            Text(
                text = pokemon.name.replaceFirstChar { it.uppercase() },
                maxLines = 1,
                fontSize = 12.sp,
                fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
                modifier = Modifier
            )
        }
    }
}