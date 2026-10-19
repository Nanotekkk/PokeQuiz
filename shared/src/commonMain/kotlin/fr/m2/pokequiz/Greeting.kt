package fr.m2.pokequiz

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Bonjour depuis ${platform.name}"
    }
}
