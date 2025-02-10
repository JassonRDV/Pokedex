package com.example.pokedex.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.pokedex.viewmodel.PokemonViewModel
import com.example.pokedex.viewmodel.UiState

@Composable
fun PokemonDetailScreen(
    viewModel: PokemonViewModel,
    modifier: Modifier = Modifier,
    uiState: UiState
) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
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
                    Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                    contentDescription = "Voltar",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .size(200.dp)
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
                    Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    contentDescription = "Voltar",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .size(200.dp)
                )
            }
            AsyncImage(
                model = uiState.selectedPokemon?.sprites?.frontDefault ?: "",
                contentDescription = uiState.selectedPokemon?.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(48.dp),
            )
            Log.d("PokemonDetailScreen", "PokemonDetailScreen: ${uiState.selectedPokemon?.sprites?.frontDefault}")
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Text(
                    text = uiState.selectedPokemon?.name.toString()
                        .replaceFirstChar { it.uppercase() },
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .padding(4.dp)
                )
                Text(
                    text = uiState.selectedPokemon?.types?.joinToString(" • ")
                    { it.type.name } ?: "Escondido",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .padding(4.dp)
                )
            }
        }

    }
}