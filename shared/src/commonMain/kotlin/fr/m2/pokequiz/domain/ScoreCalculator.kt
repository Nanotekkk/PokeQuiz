package fr.m2.pokequiz.domain

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

object ScoreCalculator {
    const val POINTS_NO_HINT = 10
    const val POINTS_WITH_HINT = 5
    const val FAST_BONUS = 1
    val FAST_THRESHOLD: Duration = 10.seconds

    fun points(correct: Boolean, hintUsed: Boolean, elapsed: Duration): Int {
        if (!correct) return 0
        val basePoints = if (hintUsed) POINTS_WITH_HINT else POINTS_NO_HINT
        val bonus = if (elapsed < FAST_THRESHOLD) FAST_BONUS else 0
        return basePoints + bonus
    }
}
