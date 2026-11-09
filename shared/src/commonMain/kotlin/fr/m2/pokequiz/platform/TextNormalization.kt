package fr.m2.pokequiz.platform

/** Retire les accents et autres marques diacritiques : "Évoli" -> "Evoli" */
expect fun String.removeDiacritics(): String
