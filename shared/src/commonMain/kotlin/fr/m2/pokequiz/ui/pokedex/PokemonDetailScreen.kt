package fr.m2.pokequiz.ui.pokedex

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.m2.pokequiz.domain.Pokemon
import fr.m2.pokequiz.resources.Res
import fr.m2.pokequiz.resources.back
import fr.m2.pokequiz.resources.detail_types
import fr.m2.pokequiz.resources.pokemon_number
import fr.m2.pokequiz.ui.PokemonImage
import fr.m2.pokequiz.ui.theme.QuizColors
import org.jetbrains.compose.resources.stringResource

@Composable
fun PokemonDetailScreen(
    pokemon: Pokemon,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PokemonImage(
            url = pokemon.imageUrl,
            grayscale = false,
            modifier = Modifier.widthIn(max = 320.dp).aspectRatio(1f)
        )
        Text(
            text = stringResource(Res.string.pokemon_number, pokemon.id),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = pokemon.nameFr,
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = pokemon.nameEn,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (pokemon.types.isNotEmpty()) {
            Text(
                text = stringResource(Res.string.detail_types),
                style = MaterialTheme.typography.titleSmall
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                pokemon.types.forEach { type ->
                    AssistChip(
                        onClick = {},
                        label = { Text(type.replaceFirstChar { it.uppercase() }) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = QuizColors.typeColor(type),
                            labelColor = QuizColors.onTypeColor(type)
                        ),
                        border = null
                    )
                }
            }
        }
        OutlinedButton(onClick = onBack) {
            Text(stringResource(Res.string.back))
        }
    }
}
