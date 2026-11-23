package fr.m2.pokequiz.ui.leaderboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fr.m2.pokequiz.domain.Score
import fr.m2.pokequiz.resources.Res
import fr.m2.pokequiz.resources.leaderboard_empty
import fr.m2.pokequiz.resources.leaderboard_points
import fr.m2.pokequiz.resources.leaderboard_pseudo
import fr.m2.pokequiz.resources.leaderboard_time
import fr.m2.pokequiz.ui.formatDuration
import org.jetbrains.compose.resources.stringResource

@Composable
fun LeaderboardScreen(
    scores: List<Score>,
    highlightId: Long?,
    modifier: Modifier = Modifier
) {
    if (scores.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(Res.string.leaderboard_empty),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize().padding(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("#", modifier = Modifier.width(40.dp), fontWeight = FontWeight.Bold)
                    Text(stringResource(Res.string.leaderboard_pseudo), modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                    Text(stringResource(Res.string.leaderboard_points), modifier = Modifier.width(80.dp), fontWeight = FontWeight.Bold)
                    Text(stringResource(Res.string.leaderboard_time), modifier = Modifier.width(60.dp), fontWeight = FontWeight.Bold)
                }
                HorizontalDivider()
            }
            itemsIndexed(scores, key = { _, s -> s.id }) { index, score ->
                val bg = if (score.id == highlightId) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.surfaceContainer
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(bg)
                        .padding(vertical = 12.dp, horizontal = 8.dp)
                        .semantics(mergeDescendants = true) {},
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("#${index + 1}", modifier = Modifier.width(40.dp))
                    Text(
                        text = score.pseudo,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = if (score.id == highlightId) FontWeight.Bold else FontWeight.Normal
                    )
                    Text("${score.points} pts", modifier = Modifier.width(80.dp))
                    Text(formatDuration(score.totalTime), modifier = Modifier.width(60.dp))
                }
                HorizontalDivider()
            }
        }
    }
}
