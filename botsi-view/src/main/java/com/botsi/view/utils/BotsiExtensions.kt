package com.botsi.view.utils

import com.google.gson.JsonElement
import java.util.Locale

fun JsonElement.toIntList(): List<Int> = this.asString.split(" ").map { it.toInt() }

fun JsonElement.toCapitalizedString(): String = asString.replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
}