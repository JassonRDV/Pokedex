package com.example.pokedex

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pokedex.ui.screens.PokemonDetailScreen
import com.example.pokedex.ui.screens.PokemonListScreen
import com.example.pokedex.viewmodel.PokemonViewModel

@Composable
fun PokedexApp(
    viewModel: PokemonViewModel = viewModel()
) {

    val pokemonQuery = viewModel.getFilteredPokemonList()

    Scaffold(
        topBar = {
            SmallTopAppBar(viewModel = viewModel)
        },
        containerColor = Color.Transparent,
        contentColor = Color.Transparent,
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            PokemonListScreen(
                modifier = Modifier,
                viewModel = viewModel,
                pokemonQuery = pokemonQuery,
            )
            AnimatedVisibility(
                visible = viewModel.showDetail.show,
                enter = scaleIn(),
                exit = scaleOut(),
                modifier = Modifier.align(Alignment.Center)
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmallTopAppBar(viewModel: PokemonViewModel) {

    var text by remember { mutableStateOf("") }

    TopAppBar(
        title = {
            TextField(
                value = text,
                onValueChange = { newText ->
                    text = newText
                    viewModel.updateSearchQuery(newText)
                },
                placeholder = { Text("Pesquisar Pokemon") },
                singleLine = true,
                textStyle = TextStyle(color = Color.Black),
                modifier = Modifier.fillMaxWidth(),
            )
        },
        modifier = Modifier.background(color = MaterialTheme.colorScheme.background)
    )
}
