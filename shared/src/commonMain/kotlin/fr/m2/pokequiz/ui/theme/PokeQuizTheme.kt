package fr.m2.pokequiz.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp

object QuizColors {
    val Correct = Color(0xFF2E7D32)
    val Wrong = Color(0xFFC62828)

    fun typeColor(type: String): Color = when (type.lowercase()) {
        "grass" -> Color(0xFF7AC74C)
        "fire" -> Color(0xFFEE8130)
        "water" -> Color(0xFF6390F0)
        "electric" -> Color(0xFFF7D02C)
        "normal" -> Color(0xFFA8A878)
        "fairy" -> Color(0xFFD685AD)
        "poison" -> Color(0xFFA040A0)
        "psychic" -> Color(0xFFF85888)
        else -> Color(0xFF9E9E9E)
    }

    fun onTypeColor(type: String): Color =
        if (typeColor(type).luminance() > 0.5f) Color.Black else Color.White
}

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFCC0000),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFDAD6),
    onPrimaryContainer = Color(0xFF410002),
    secondary = Color(0xFF3B4CCA),
    onSecondary = Color.White,
    surface = Color(0xFFFEF7FF),
    onSurface = Color(0xFF1D1B20)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFB4AB),
    onPrimary = Color(0xFF690005),
    primaryContainer = Color(0xFF93000A),
    onPrimaryContainer = Color(0xFFFFDAD6),
    secondary = Color(0xFFB4C5FF),
    onSecondary = Color(0xFF002A78),
    surface = Color(0xFF141218),
    onSurface = Color(0xFFE6E0E9)
)

@Composable
fun PokeQuizTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        shapes = Shapes(medium = RoundedCornerShape(12.dp)),
        content = content
    )
}
