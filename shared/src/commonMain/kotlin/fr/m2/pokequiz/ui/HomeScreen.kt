package fr.m2.pokequiz.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import fr.m2.pokequiz.resources.Res
import fr.m2.pokequiz.resources.app_name
import fr.m2.pokequiz.resources.home_leaderboard
import fr.m2.pokequiz.resources.home_pokedex
import fr.m2.pokequiz.resources.home_quiz
import fr.m2.pokequiz.resources.home_subtitle
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeScreen(
    onPokedex: () -> Unit,
    onQuiz: () -> Unit,
    onLeaderboard: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Text(
            text = stringResource(Res.string.app_name),
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.semantics { heading() }
        )
        Text(
            text = stringResource(Res.string.home_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Button(onClick = onQuiz) {
            Text(stringResource(Res.string.home_quiz))
        }
        FilledTonalButton(onClick = onPokedex) {
            Text(stringResource(Res.string.home_pokedex))
        }
        FilledTonalButton(onClick = onLeaderboard) {
            Text(stringResource(Res.string.home_leaderboard))
        }
    }
}
