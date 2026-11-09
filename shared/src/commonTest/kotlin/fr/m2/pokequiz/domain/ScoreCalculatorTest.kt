package fr.m2.pokequiz.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class ScoreCalculatorTest {
    @Test
    fun correctWithoutHintFast() {
        assertEquals(11, ScoreCalculator.points(correct = true, hintUsed = false, elapsed = 3.seconds))
    }

    @Test
    fun correctWithoutHintSlow() {
        assertEquals(10, ScoreCalculator.points(correct = true, hintUsed = false, elapsed = 15.seconds))
    }

    @Test
    fun correctWithHintFast() {
        assertEquals(6, ScoreCalculator.points(correct = true, hintUsed = true, elapsed = 5.seconds))
    }

    @Test
    fun correctWithHintSlow() {
        assertEquals(5, ScoreCalculator.points(correct = true, hintUsed = true, elapsed = 12.seconds))
    }

    @Test
    fun incorrectFastWithoutHint() {
        assertEquals(0, ScoreCalculator.points(correct = false, hintUsed = false, elapsed = 4.seconds))
    }

    @Test
    fun incorrectWithHint() {
        assertEquals(0, ScoreCalculator.points(correct = false, hintUsed = true, elapsed = 3.seconds))
    }

    @Test
    fun exactlyTenSecondsGivesNoBonus() {
        assertEquals(10, ScoreCalculator.points(correct = true, hintUsed = false, elapsed = 10.seconds))
    }

    @Test
    fun justUnderTenSecondsGivesBonus() {
        assertEquals(11, ScoreCalculator.points(correct = true, hintUsed = false, elapsed = 9_999.milliseconds))
    }
}
