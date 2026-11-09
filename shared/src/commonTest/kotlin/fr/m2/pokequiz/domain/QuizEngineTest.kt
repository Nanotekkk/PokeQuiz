package fr.m2.pokequiz.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class QuizEngineTest {
    private fun pokemon(id: Int, fr: String, en: String) = Pokemon(
        id = id, nameFr = fr, nameEn = en,
        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
    )

    private val pokemons = listOf(
        pokemon(1, "Bulbizarre", "Bulbasaur"),
        pokemon(4, "Salamèche", "Charmander"),
        pokemon(7, "Carapuce", "Squirtle"),
        pokemon(25, "Pikachu", "Pikachu"),
        pokemon(133, "Évoli", "Eevee"),
    )

    @Test
    fun initialState() {
        val engine = QuizEngine(pokemons)
        assertEquals(5, engine.total)
        assertEquals(0, engine.currentIndex)
        assertEquals("Bulbizarre", engine.current?.nameFr)
        assertFalse(engine.isFinished)
        assertEquals(0, engine.totalPoints)
        assertEquals(Duration.ZERO, engine.totalTime)
    }

    @Test
    fun fullGameComputesTotals() {
        val engine = QuizEngine(pokemons)
        engine.submit("bulbizarre", hintUsed = false, elapsed = 3.seconds)
        engine.submit("salameche", hintUsed = true, elapsed = 12.seconds)
        engine.submit("tortank", hintUsed = false, elapsed = 4.seconds)
        engine.submit("PIKACHU", hintUsed = false, elapsed = 10.seconds)
        engine.submit(" evoli ", hintUsed = true, elapsed = 2.seconds)

        assertEquals(32, engine.totalPoints)
        assertEquals(31.seconds, engine.totalTime)
        assertTrue(engine.isFinished)
        assertNull(engine.current)
        assertEquals(5, engine.currentIndex)
    }

    @Test
    fun submitAfterEndThrows() {
        val engine = QuizEngine(pokemons)
        engine.submit("bulbizarre", hintUsed = false, elapsed = 3.seconds)
        engine.submit("salameche", hintUsed = false, elapsed = 3.seconds)
        engine.submit("carapuce", hintUsed = false, elapsed = 3.seconds)
        engine.submit("pikachu", hintUsed = false, elapsed = 3.seconds)
        engine.submit("evoli", hintUsed = false, elapsed = 3.seconds)

        assertFailsWith<IllegalStateException> {
            engine.submit("autre", hintUsed = false, elapsed = 1.seconds)
        }
    }

    @Test
    fun emptyListIsRejected() {
        assertFailsWith<IllegalArgumentException> {
            QuizEngine(emptyList())
        }
    }
}
