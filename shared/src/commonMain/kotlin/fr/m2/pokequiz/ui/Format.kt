package fr.m2.pokequiz.ui

import kotlin.time.Duration

fun formatDuration(duration: Duration): String {
    val totalSeconds = duration.inWholeSeconds
    val mins = totalSeconds / 60
    val secs = (totalSeconds % 60).toString().padStart(2, '0')
    return "$mins:$secs"
}
