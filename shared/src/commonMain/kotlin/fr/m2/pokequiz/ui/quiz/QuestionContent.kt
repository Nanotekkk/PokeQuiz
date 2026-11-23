package fr.m2.pokequiz.ui.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import fr.m2.pokequiz.domain.Pokemon
import fr.m2.pokequiz.resources.Res
import fr.m2.pokequiz.resources.answer_label
import fr.m2.pokequiz.resources.elapsed_label
import fr.m2.pokequiz.resources.hint_button
import fr.m2.pokequiz.resources.hint_label
import fr.m2.pokequiz.resources.home_subtitle
import fr.m2.pokequiz.resources.points
import fr.m2.pokequiz.resources.question_progress
import fr.m2.pokequiz.resources.submit
import fr.m2.pokequiz.ui.PokemonImage
import fr.m2.pokequiz.ui.formatDuration
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration

@Composable
fun QuestionContent(
    index: Int,
    total: Int,
    pokemon: Pokemon,
    input: String,
    hint: String?,
    elapsed: Duration,
    totalPoints: Int,
    onInputChange: (String) -> Unit,
    onHint: () -> Unit,
    onSubmit: () -> Unit,
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
        Column(
            modifier = Modifier.widthIn(max = 560.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(Res.string.question_progress, index + 1, total),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = pluralStringResource(Res.plurals.points, totalPoints, totalPoints),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            LinearProgressIndicator(
                progress = { (index + 1) / total.toFloat() },
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = stringResource(Res.string.elapsed_label, formatDuration(elapsed)),
                style = MaterialTheme.typography.bodyMedium
            )
            PokemonImage(
                url = pokemon.imageUrl,
                grayscale = true,
                modifier = Modifier.size(240.dp)
            )
            Text(
                text = stringResource(Res.string.home_subtitle),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.semantics { heading() }
            )
            OutlinedTextField(
                value = input,
                onValueChange = onInputChange,
                label = { Text(stringResource(Res.string.answer_label)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { if (input.isNotBlank()) onSubmit() }),
                modifier = Modifier.fillMaxWidth().testTag("quiz_answer")
            )
            if (hint != null) {
                Text(
                    text = stringResource(Res.string.hint_label, hint),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onHint,
                    enabled = hint == null,
                    modifier = Modifier.weight(1f).testTag("quiz_hint")
                ) {
                    Text(stringResource(Res.string.hint_button))
                }
                Button(
                    onClick = onSubmit,
                    enabled = input.isNotBlank(),
                    modifier = Modifier.weight(1f).testTag("quiz_submit")
                ) {
                    Text(stringResource(Res.string.submit))
                }
            }
        }
    }
}
