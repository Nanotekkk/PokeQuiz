package fr.m2.pokequiz.domain

import fr.m2.pokequiz.platform.removeDiacritics

object AnswerChecker {
    private val NON_ALPHANUMERIC = Regex("[^a-z0-9]")

    fun normalize(input: String): String {
        return input.trim()
            .lowercase()
            .removeDiacritics()
            .replace(NON_ALPHANUMERIC, "")
    }

    fun isCorrect(input: String, expected: String): Boolean {
        val normalizedInput = normalize(input)
        val normalizedExpected = normalize(expected)
        return normalizedInput.isNotEmpty() && normalizedInput == normalizedExpected
    }

    fun hint(name: String): String {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return ""
        return trimmed.first().uppercase()
    }
}
