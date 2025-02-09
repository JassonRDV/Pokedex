package com.example.pokedex

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.pokedex.ui.screens.PokemonDetailScreen
import com.example.pokedex.viewmodel.Pokemon
import com.example.pokedex.viewmodel.PokemonViewModel

sealed class Screen(val route: String) {
    object PokemonList : Screen("pokemonList")
    object PokemonDetail : Screen("pokemonDetail")
}

@Composable
fun PokedexApp(
    viewModel: PokemonViewModel = viewModel(), modifier: Modifier = Modifier
) {
    val pokemonList = viewModel.pokemonList

    LaunchedEffect(Unit) {
        viewModel.loadPokemonList(limit = viewModel.showDetail.limit, offset = 0)
    }

    Scaffold(
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            PokemonGrid(
                modifier, pokemonList, viewModel
            )
        }
        AnimatedVisibility(
            visible = viewModel.showDetail.show,
            enter = scaleIn(),
            exit = scaleOut(),
            modifier = Modifier
        ) {
            PokemonDetailScreen(
                viewModel,
                modifier = Modifier
                    .padding(40.dp)
                    .aspectRatio(1f)
                    .clickable(
                        onClick = {
                            viewModel.hideDetail()
                        }
                    )
            )
        }
    }
}

@Composable
private fun PokemonGrid(
    modifier: Modifier,
    pokemonList: List<Pokemon>,
    viewModel: PokemonViewModel
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = modifier
            .padding(4.dp)
            .fillMaxSize()
    ) {
        items(pokemonList.size) { index ->
            val pokemon = pokemonList[index]
            PokemonItem(pokemon, viewModel, index)
        }
        item(
            span = { GridItemSpan(4) }
        ) {
            Button(
                onClick = {
                    viewModel.loadMore()
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                Text("Carregar mais")
            }
        }
    }
}

@Composable
private fun PokemonItem(
    pokemon: Pokemon,
    viewModel: PokemonViewModel,
    index: Int,
) {
    Card(elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .then(
                if (!viewModel.showDetail.show) {
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
                .padding(4.dp)
        )
        Text(
            pokemon.name,
            maxLines = 1,
            fontSize = 12.sp,
            modifier = Modifier
                .padding(4.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}
