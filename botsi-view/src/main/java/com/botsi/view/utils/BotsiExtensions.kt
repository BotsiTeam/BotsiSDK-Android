package com.botsi.view.utils

import com.botsi.view.model.content.BotsiColor
import com.botsi.view.model.content.BotsiColorBehaviour
import com.botsi.view.model.content.BotsiGradient
import com.botsi.view.model.content.BotsiGradientColor
import com.google.gson.JsonElement
import java.util.Locale
import kotlin.math.roundToInt

fun JsonElement.toIntList(): List<Int> = this.asString.split(" ").map { it.toInt() }

fun JsonElement.toCapitalizedString(): String = asString.replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
}

fun JsonElement.toHexColorIfPossible(): String = asString.toHexColorIfPossible()

fun String.toHexColorIfPossible(): String = run {
    if (startsWith("#")) return@run this

    // Comma-separated rgba patterns (existing)
    val regexA = Regex("""rgb\(\s*(\d+)\s*,\s*(\d+)\s*,\s*(\d+)\s*,\s*([\d.]+)\s*\)""")
    val regexB = Regex("""rgba\(\s*(\d+)\s*,\s*(\d+)\s*,\s*(\d+)\s*,\s*([\d.]+)\s*\)""")

    // Space-separated rgba patterns (new)
    val regexC = Regex("""rgb\(\s*(\d+)\s+(\d+)\s+(\d+)\s+(\d+(?:\.\d+)?)\s*\)""")
    val regexD = Regex("""rgba\(\s*(\d+)\s+(\d+)\s+(\d+)\s+(\d+(?:\.\d+)?)\s*\)""")
    val regexE = Regex("""rgb\(\s*(\d+)\s+(\d+)\s+(\d+)\s*\)""")
    val regexF = Regex("""rgba\(\s*(\d+)\s+(\d+)\s+(\d+)\s*\)""")

    val matchResultA = regexA.find(this.trim())
    val matchResultB = regexB.find(this.trim())
    val matchResultC = regexC.find(this.trim())
    val matchResultD = regexD.find(this.trim())
    val matchResultE = regexE.find(this.trim())
    val matchResultF = regexF.find(this.trim())

    val matchResult = matchResultA ?: matchResultB ?: matchResultC ?: matchResultD ?: matchResultE ?: matchResultF

    return matchResult?.let { match ->
        val red = match.groupValues[1].toInt().coerceIn(0, 255)
        val green = match.groupValues[2].toInt().coerceIn(0, 255)
        val blue = match.groupValues[3].toInt().coerceIn(0, 255)

        // Handle alpha value - some patterns have 4 groups, some have 3
        val alpha = if (match.groupValues.size > 4 && match.groupValues[4].isNotEmpty()) {
            match.groupValues[4].toFloat().coerceIn(0f, 1f)
        } else {
            1f // Default alpha for rgb without alpha or rgba without explicit alpha
        }

        val alphaInt = (alpha * 255).roundToInt()

        String.format("#%02X%02X%02X%02X", alphaInt, red, green, blue)
    } ?: this
}

internal fun JsonElement.toGradientIfPossible(): BotsiGradient? = asString.run {
    val trimmed = this.trim()

    if (!trimmed.startsWith("linear-gradient(")) return null

    try {
        val gradientContent = trimmed
            .substringAfter("linear-gradient(")
            .substringBeforeLast(")")

        // Parse degrees (first part before first comma)
        val degreesMatch = Regex("""^(\d+)deg""").find(gradientContent)
        val degrees = degreesMatch?.groupValues?.get(1)?.toFloatOrNull()

        // Find all rgba/rgb color definitions with their positions
        val colorPattern = Regex("""(rgba?\([^)]+\))\s*(\d+)%""")
        val colorMatches = colorPattern.findAll(gradientContent).toList()

        if (colorMatches.size < 2) return null

        val colors = colorMatches.map { match ->
            val colorString = match.groupValues[1]
            val position = match.groupValues[2].toFloatOrNull()

            BotsiGradientColor(
                color = colorString.toHexColorIfPossible(),
                position = position
            )
        }

        return@run BotsiGradient(
            degrees = degrees,
            colors = colors
        )
    } catch (_: Exception) {
        return@run null
    }
}

internal fun JsonElement.toFillBehaviourIfPossible(): BotsiColorBehaviour? {
    // First try to parse as gradient
    val gradient = toGradientIfPossible()
    if (gradient != null) {
        return gradient
    }

    // If not a gradient, try to parse as a simple color
    val color = toHexColorIfPossible()
    return if (color.isNotBlank()) {
        BotsiColor(color)
    } else null
}
