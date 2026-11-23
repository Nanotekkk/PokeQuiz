package fr.m2.pokequiz.data

import fr.m2.pokequiz.domain.Score
import kotlin.time.Duration.Companion.seconds

object FakeScores {
    val initial: List<Score> = listOf(
        Score(id = 1, pseudo = "Red", points = 55, totalTime = 18.seconds),
        Score(id = 2, pseudo = "Blue", points = 48, totalTime = 22.seconds),
        Score(id = 3, pseudo = "Sacha", points = 42, totalTime = 25.seconds),
        Score(id = 4, pseudo = "Ondine", points = 35, totalTime = 30.seconds),
        Score(id = 5, pseudo = "Pierre", points = 28, totalTime = 32.seconds)
    )
}
