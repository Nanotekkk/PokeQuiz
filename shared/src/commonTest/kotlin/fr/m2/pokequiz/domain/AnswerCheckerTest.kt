package fr.m2.pokequiz.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AnswerCheckerTest {
    @Test
    fun ignoresCaseAndSurroundingSpaces() {
        assertTrue(AnswerChecker.isCorrect(" PIKACHU ", "Pikachu"))
    }

    @Test
    fun ignoresAccents() {
        assertTrue(AnswerChecker.isCorrect("salameche", "Salamèche"))
        assertTrue(AnswerChecker.isCorrect("evoli", "Évoli"))
    }

    @Test
    fun removesPunctuationAndSpaces() {
        assertEquals("mmime", AnswerChecker.normalize("M. Mime"))
    }

    @Test
    fun emptyOrBlankInputIsNeverCorrect() {
        assertFalse(AnswerChecker.isCorrect("", "Pikachu"))
        assertFalse(AnswerChecker.isCorrect("   ", "Pikachu"))
    }

    @Test
    fun wrongNameIsRejected() {
        assertFalse(AnswerChecker.isCorrect("Carapuce", "Salamèche"))
    }

    @Test
    fun hintIsFirstLetterUppercase() {
        assertEquals("É", AnswerChecker.hint("évoli"))
        assertEquals("P", AnswerChecker.hint("Pikachu"))
        assertEquals("", AnswerChecker.hint(""))
    }

    @Test
    fun nidoranFemaleAndMaleCollide() {
        assertEquals("nidoran", AnswerChecker.normalize("Nidoran♀"))
        assertEquals("nidoran", AnswerChecker.normalize("Nidoran♂"))
    }
}
