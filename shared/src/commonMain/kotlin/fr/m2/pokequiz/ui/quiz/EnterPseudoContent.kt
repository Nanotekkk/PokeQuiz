package fr.m2.pokequiz.ui.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.m2.pokequiz.resources.Res
import fr.m2.pokequiz.resources.points
import fr.m2.pokequiz.resources.pseudo_label
import fr.m2.pokequiz.resources.pseudo_save
import fr.m2.pokequiz.resources.pseudo_title
import fr.m2.pokequiz.resources.total_time
import fr.m2.pokequiz.ui.formatDuration
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration

@Composable
fun EnterPseudoContent(
    totalPoints: Int,
    totalTime: Duration,
    pseudo: String,
    error: String?,
    onPseudoChange: (String) -> Unit,
    onSave: () -> Unit,
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
            Text(
                text = stringResource(Res.string.pseudo_title),
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = pluralStringResource(Res.plurals.points, totalPoints, totalPoints),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = stringResource(Res.string.total_time, formatDuration(totalTime)),
                style = MaterialTheme.typography.bodyLarge
            )
            OutlinedTextField(
                value = pseudo,
                onValueChange = onPseudoChange,
                label = { Text(stringResource(Res.string.pseudo_label)) },
                isError = error != null,
                supportingText = if (error != null) { { Text(error) } } else null,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(Res.string.pseudo_save))
            }
        }
    }
}
