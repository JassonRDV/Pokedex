package com.example.pokedex.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.pokedex.viewmodel.PokemonType
import com.example.pokedex.viewmodel.PokemonViewModel
import com.example.pokedex.viewmodel.Sprites

@Composable
fun PokemonDetailScreen(
    viewModel: PokemonViewModel,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            IconButton(
                onClick = {
                    viewModel.previousPokemonDetail()
                },
                modifier = Modifier
                    .align(Alignment.CenterStart)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Voltar",
                    tint = Color.Black,
                    modifier = Modifier
                )
            }
            IconButton(
                onClick = {
                    viewModel.nextPokemonDetail()
                },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Voltar",
                    tint = Color.Black,
                    modifier = Modifier
                )
            }
            AsyncImage(
                model = viewModel.selectedPokemon?.sprites?.front_default,
                contentDescription = viewModel.selectedPokemon?.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
            )
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = modifier.fillMaxSize()
            ) {
                Text(
                    text = viewModel.selectedPokemon?.name.toString(),
                    modifier = Modifier
                        .padding(4.dp)
                )
                Text(
                    text = viewModel.selectedPokemon?.types?.joinToString(", ")
                    { it.type.name } ?: "Desconhecido",
                    modifier = Modifier
                        .padding(4.dp)
                )
            }
        }
    }
}