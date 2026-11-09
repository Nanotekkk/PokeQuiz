package fr.m2.pokequiz.platform

import kotlin.test.Test
import kotlin.test.assertEquals

class TextNormalizationTest {
    @Test
    fun removesFrenchAccents() {
        assertEquals("Salameche", "Salamèche".removeDiacritics())
        assertEquals("Evoli", "Évoli".removeDiacritics())
        assertEquals("aeiouc", "àéîõüç".removeDiacritics())
    }

    @Test
    fun keepsPlainAscii() {
        assertEquals("Pikachu", "Pikachu".removeDiacritics())
    }
}
