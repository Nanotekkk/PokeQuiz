package fr.m2.pokequiz.platform

import java.text.Normalizer

private val COMBINING_MARKS = Regex("\\p{Mn}+")

actual fun String.removeDiacritics(): String {
    val normalized = Normalizer.normalize(this, Normalizer.Form.NFD)
    return COMBINING_MARKS.replace(normalized, "")
}
