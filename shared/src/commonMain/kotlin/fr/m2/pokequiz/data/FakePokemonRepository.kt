package fr.m2.pokequiz.data

import fr.m2.pokequiz.domain.Pokemon
import fr.m2.pokequiz.domain.PokemonRepository
import fr.m2.pokequiz.domain.PokemonSummary
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

private const val ARTWORK_BASE = "https://raw.githubusercontent.com/Yarkis01/TyraDex/images/sprites"

fun artworkUrl(id: Int): String = "$ARTWORK_BASE/$id/regular.png"

object FakePokemons {
    val all: List<Pokemon> = listOf(
        pokemon(1, "Bulbizarre", "Bulbasaur", "grass", "poison"),
        pokemon(4, "Salamèche", "Charmander", "fire"),
        pokemon(7, "Carapuce", "Squirtle", "water"),
        pokemon(25, "Pikachu", "Pikachu", "electric"),
        pokemon(39, "Rondoudou", "Jigglypuff", "normal", "fairy"),
        pokemon(52, "Miaouss", "Meowth", "normal"),
        pokemon(54, "Psykokwak", "Psyduck", "water"),
        pokemon(133, "Évoli", "Eevee", "normal"),
        pokemon(143, "Ronflex", "Snorlax", "normal"),
        pokemon(150, "Mewtwo", "Mewtwo", "psychic")
    )

    val pikachu: Pokemon get() = all.first { it.id == 25 }

    private fun pokemon(id: Int, nameFr: String, nameEn: String, vararg types: String) =
        Pokemon(
            id = id,
            nameFr = nameFr,
            nameEn = nameEn,
            imageUrl = artworkUrl(id),
            types = types.toList()
        )
}

class FakePokemonRepository(
    private val latency: Duration = 300.milliseconds
) : PokemonRepository {
    private val all = FakePokemons.all

    override suspend fun speciesCount(): Int = all.size

    override suspend fun pokemon(id: Int): Pokemon {
        delay(latency)
        return all.firstOrNull { it.id == id }
            ?: throw NoSuchElementException("Pokémon avec l'id $id introuvable")
    }

    override suspend fun page(offset: Int, limit: Int): List<PokemonSummary> {
        delay(latency)
        return all.drop(offset).take(limit).map {
            PokemonSummary(id = it.id, name = it.nameEn, imageUrl = it.imageUrl)
        }
    }

    override suspend fun randomPokemons(count: Int): List<Pokemon> {
        delay(latency)
        return all.shuffled().take(count)
    }
}
