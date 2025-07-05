package com.botsi.view.utils

import com.google.gson.JsonElement
import java.util.Locale
import kotlin.math.roundToInt

fun JsonElement.toIntList(): List<Int> = this.asString.split(" ").map { it.toInt() }

fun JsonElement.toCapitalizedString(): String = asString.replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
}

fun JsonElement.toHexColorIfPossible(): String = asString.run {
    if (startsWith("#")) return@run this

    val regex = Regex("""rgba\(\s*(\d+)\s*,\s*(\d+)\s*,\s*(\d+)\s*,\s*([\d.]+)\s*\)""")
    val matchResult = regex.find(this.trim())

    return matchResult?.let { match ->
        val red = match.groupValues[1].toInt().coerceIn(0, 255)
        val green = match.groupValues[2].toInt().coerceIn(0, 255)
        val blue = match.groupValues[3].toInt().coerceIn(0, 255)
        val alpha = match.groupValues[4].toFloat().coerceIn(0f, 1f)

        val alphaInt = (alpha * 255).roundToInt()

        String.format("#%02X%02X%02X%02X", alphaInt, red, green, blue)
    } ?: this
}