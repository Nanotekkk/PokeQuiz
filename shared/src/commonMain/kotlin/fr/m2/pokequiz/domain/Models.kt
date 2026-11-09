package fr.m2.pokequiz.domain

import kotlin.time.Duration

const val QUESTIONS_PER_GAME = 5

data class Pokemon(
    val id: Int,
    val nameFr: String,
    val nameEn: String,
    val imageUrl: String,
    val types: List<String> = emptyList(),
)

data class PokemonSummary(
    val id: Int,
    val name: String,
    val imageUrl: String,
)

data class QuestionResult(
    val pokemon: Pokemon,
    val answer: String,
    val correct: Boolean,
    val hintUsed: Boolean,
    val elapsed: Duration,
    val points: Int,
)
