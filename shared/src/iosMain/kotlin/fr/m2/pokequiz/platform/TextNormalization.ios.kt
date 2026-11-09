package fr.m2.pokequiz.platform

import platform.Foundation.NSString
import platform.Foundation.decomposedStringWithCanonicalMapping

@Suppress("CAST_NEVER_SUCCEEDS")
actual fun String.removeDiacritics(): String {
    val decomposed: String = (this as NSString).decomposedStringWithCanonicalMapping
    return decomposed.filterNot { it.category == CharCategory.NON_SPACING_MARK }
}
