package fr.m2.pokequiz

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import fr.m2.pokequiz.data.FakePokemonRepository
import fr.m2.pokequiz.data.FakeScores
import fr.m2.pokequiz.domain.AnswerChecker
import fr.m2.pokequiz.domain.Pokemon
import fr.m2.pokequiz.domain.PokemonRepository
import fr.m2.pokequiz.domain.PokemonSummary
import fr.m2.pokequiz.domain.QuestionResult
import fr.m2.pokequiz.domain.QuizEngine
import fr.m2.pokequiz.domain.Score
import fr.m2.pokequiz.resources.Res
import fr.m2.pokequiz.resources.app_name
import fr.m2.pokequiz.resources.detail_title
import fr.m2.pokequiz.resources.leaderboard_title
import fr.m2.pokequiz.resources.pokedex_title
import fr.m2.pokequiz.resources.pseudo_error_blank
import fr.m2.pokequiz.resources.pseudo_error_length
import fr.m2.pokequiz.resources.quiz_title
import fr.m2.pokequiz.ui.HomeScreen
import fr.m2.pokequiz.ui.leaderboard.LeaderboardScreen
import fr.m2.pokequiz.ui.pokedex.PokedexScreen
import fr.m2.pokequiz.ui.pokedex.PokemonDetailScreen
import fr.m2.pokequiz.ui.quiz.EnterPseudoContent
import fr.m2.pokequiz.ui.quiz.QuestionContent
import fr.m2.pokequiz.ui.quiz.RevealContent
import fr.m2.pokequiz.ui.theme.PokeQuizTheme
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration

sealed interface Screen {
    data object Home : Screen
    data object Pokedex : Screen
    data class Detail(val id: Int) : Screen
    data object Quiz : Screen
    data class Leaderboard(val highlightId: Long? = null) : Screen
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    PokeQuizTheme {
        val repository = remember { FakePokemonRepository() }
        var screen by remember { mutableStateOf<Screen>(Screen.Home) }
        val scores = remember { mutableStateListOf<Score>().apply { addAll(FakeScores.initial) } }
        val ranking by remember {
            derivedStateOf {
                scores.sortedWith(compareByDescending<Score> { it.points }.thenBy { it.totalTime }).take(20)
            }
        }

        val title = when (screen) {
            Screen.Home -> stringResource(Res.string.app_name)
            Screen.Pokedex -> stringResource(Res.string.pokedex_title)
            is Screen.Detail -> stringResource(Res.string.detail_title)
            Screen.Quiz -> stringResource(Res.string.quiz_title)
            is Screen.Leaderboard -> stringResource(Res.string.leaderboard_title)
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(title) },
                    navigationIcon = {
                        if (screen != Screen.Home) {
                            IconButton(onClick = { screen = Screen.Home }) {
                                Text("←", style = MaterialTheme.typography.titleLarge)
                            }
                        }
                    }
                )
            }
        ) { innerPadding ->
            Box(Modifier.fillMaxSize().padding(innerPadding)) {
                when (val current = screen) {
                    Screen.Home -> HomeScreen(
                        onPokedex = { screen = Screen.Pokedex },
                        onQuiz = { screen = Screen.Quiz },
                        onLeaderboard = { screen = Screen.Leaderboard() }
                    )
                    Screen.Pokedex -> PokedexRoute(repository, onClick = { id -> screen = Screen.Detail(id) })
                    is Screen.Detail -> DetailRoute(repository, current.id, onBack = { screen = Screen.Pokedex })
                    Screen.Quiz -> QuizRoute(
                        repository = repository,
                        onSaved = { newScore ->
                            val newId = (scores.maxOfOrNull { it.id } ?: 0L) + 1L
                            val scoreWithId = newScore.copy(id = newId)
                            scores.add(scoreWithId)
                            screen = Screen.Leaderboard(highlightId = newId)
                        }
                    )
                    is Screen.Leaderboard -> LeaderboardScreen(ranking, current.highlightId)
                }
            }
        }
    }
}

@Composable
private fun LoadingBox() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun PokedexRoute(repository: PokemonRepository, onClick: (Int) -> Unit) {
    var items by remember { mutableStateOf<List<PokemonSummary>?>(null) }
    LaunchedEffect(Unit) {
        items = repository.page(offset = 0, limit = 50)
    }
    val loaded = items
    if (loaded == null) LoadingBox() else PokedexScreen(items = loaded, onClick = onClick)
}

@Composable
private fun DetailRoute(repository: PokemonRepository, id: Int, onBack: () -> Unit) {
    var pokemon by remember { mutableStateOf<Pokemon?>(null) }
    LaunchedEffect(id) {
        pokemon = repository.pokemon(id)
    }
    val loaded = pokemon
    if (loaded == null) LoadingBox() else PokemonDetailScreen(pokemon = loaded, onBack = onBack)
}

private sealed interface QuizPhase {
    data object Question : QuizPhase
    data class Reveal(val result: QuestionResult) : QuizPhase
    data object EnterPseudo : QuizPhase
}

@Composable
private fun QuizRoute(repository: PokemonRepository, onSaved: (Score) -> Unit) {
    var engine by remember { mutableStateOf<QuizEngine?>(null) }
    var phase by remember { mutableStateOf<QuizPhase>(QuizPhase.Question) }
    var input by remember { mutableStateOf("") }
    var hintUsed by remember { mutableStateOf(false) }
    var pseudo by remember { mutableStateOf("") }
    var pseudoError by remember { mutableStateOf<String?>(null) }

    val errBlank = stringResource(Res.string.pseudo_error_blank)
    val errLength = stringResource(Res.string.pseudo_error_length)

    LaunchedEffect(Unit) {
        engine = QuizEngine(repository.randomPokemons())
    }

    val quiz = engine
    if (quiz == null) {
        LoadingBox()
        return
    }

    when (val p = phase) {
        QuizPhase.Question -> {
            val pokemon = quiz.current ?: return
            QuestionContent(
                index = quiz.currentIndex,
                total = quiz.total,
                pokemon = pokemon,
                input = input,
                hint = if (hintUsed) AnswerChecker.hint(pokemon.nameFr) else null,
                elapsed = Duration.ZERO,
                totalPoints = quiz.totalPoints,
                onInputChange = { input = it },
                onHint = { hintUsed = true },
                onSubmit = {
                    val result = quiz.submit(input, hintUsed, Duration.ZERO)
                    input = ""
                    hintUsed = false
                    phase = QuizPhase.Reveal(result)
                }
            )
        }
        is QuizPhase.Reveal -> {
            RevealContent(
                result = p.result,
                index = quiz.results.size - 1,
                total = quiz.total,
                totalPoints = quiz.totalPoints,
                isLast = quiz.isFinished,
                onNext = {
                    if (quiz.isFinished) {
                        phase = QuizPhase.EnterPseudo
                    } else {
                        phase = QuizPhase.Question
                    }
                }
            )
        }
        QuizPhase.EnterPseudo -> {
            EnterPseudoContent(
                totalPoints = quiz.totalPoints,
                totalTime = quiz.totalTime,
                pseudo = pseudo,
                error = pseudoError,
                onPseudoChange = {
                    pseudo = it
                    pseudoError = null
                },
                onSave = {
                    val trimmed = pseudo.trim()
                    if (trimmed.isBlank()) {
                        pseudoError = errBlank
                    } else if (trimmed.length > 12) {
                        pseudoError = errLength
                    } else {
                        onSaved(Score(pseudo = trimmed, points = quiz.totalPoints, totalTime = quiz.totalTime))
                    }
                }
            )
        }
    }
}
