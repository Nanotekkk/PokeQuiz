package fr.m2.pokequiz.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import fr.m2.pokequiz.resources.Res
import fr.m2.pokequiz.resources.mystery_pokemon
import fr.m2.pokequiz.resources.pokemon_artwork
import org.jetbrains.compose.resources.stringResource

@Composable
fun PokemonImage(url: String, grayscale: Boolean, modifier: Modifier = Modifier) {
    val saturation by animateFloatAsState(
        targetValue = if (grayscale) 0f else 1f,
        animationSpec = tween(durationMillis = 700),
        label = "saturation"
    )
    val contentDescription = if (grayscale) {
        stringResource(Res.string.mystery_pokemon)
    } else {
        stringResource(Res.string.pokemon_artwork)
    }

    AsyncImage(
        model = url,
        contentDescription = contentDescription,
        colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(saturation) }),
        contentScale = ContentScale.Fit,
        modifier = modifier
    )
}
