package fr.m2.pokequiz.ui.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fr.m2.pokequiz.domain.QuestionResult
import fr.m2.pokequiz.resources.Res
import fr.m2.pokequiz.resources.next_question
import fr.m2.pokequiz.resources.points_gained
import fr.m2.pokequiz.resources.reveal_answer
import fr.m2.pokequiz.resources.reveal_correct
import fr.m2.pokequiz.resources.reveal_hint_used
import fr.m2.pokequiz.resources.reveal_wrong
import fr.m2.pokequiz.resources.reveal_your_answer
import fr.m2.pokequiz.resources.see_results
import fr.m2.pokequiz.resources.total_score
import fr.m2.pokequiz.ui.PokemonImage
import fr.m2.pokequiz.ui.theme.QuizColors
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun RevealContent(
    result: QuestionResult,
    index: Int,
    total: Int,
    totalPoints: Int,
    isLast: Boolean,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    var revealed by remember(result) { mutableStateOf(false) }
    LaunchedEffect(result) { revealed = true }

    val bannerColor = if (result.correct) QuizColors.Correct else QuizColors.Wrong
    val bannerText = if (result.correct) stringResource(Res.string.reveal_correct) else stringResource(Res.string.reveal_wrong)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            modifier = Modifier.widthIn(max = 560.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                color = bannerColor,
                contentColor = Color.White,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = bannerText,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp),
                    color = Color.White
                )
            }
            PokemonImage(
                url = result.pokemon.imageUrl,
                grayscale = !revealed,
                modifier = Modifier.size(240.dp)
            )
            Text(
                text = stringResource(Res.string.reveal_answer, result.pokemon.nameFr),
                style = MaterialTheme.typography.headlineSmall
            )
            if (!result.correct && result.answer.isNotBlank()) {
                Text(
                    text = stringResource(Res.string.reveal_your_answer, result.answer),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = pluralStringResource(Res.plurals.points_gained, result.points, result.points),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            if (result.hintUsed) {
                Text(
                    text = stringResource(Res.string.reveal_hint_used),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                text = stringResource(Res.string.total_score, totalPoints),
                style = MaterialTheme.typography.bodyLarge
            )
            Button(
                onClick = onNext,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isLast) stringResource(Res.string.see_results) else stringResource(Res.string.next_question))
            }
        }
    }
}
