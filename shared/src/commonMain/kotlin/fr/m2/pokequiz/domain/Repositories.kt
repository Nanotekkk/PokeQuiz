package fr.m2.pokequiz.domain

import kotlinx.coroutines.flow.Flow
import kotlin.time.Duration

interface PokemonRepository {
    suspend fun speciesCount(): Int
    suspend fun pokemon(id: Int): Pokemon
    suspend fun page(offset: Int, limit: Int): List<PokemonSummary>
    suspend fun randomPokemons(count: Int = QUESTIONS_PER_GAME): List<Pokemon>
}

data class Score(
    val id: Long = 0,
    val pseudo: String,
    val points: Int,
    val totalTime: Duration,
)

interface ScoreRepository {
    fun leaderboard(limit: Int = 20): Flow<List<Score>>
    suspend fun save(score: Score)
}
