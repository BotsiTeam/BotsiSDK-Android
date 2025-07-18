package com.botsi.view.mapper

import com.botsi.view.model.content.BotsiButtonStyle
import com.botsi.view.utils.toHexColorIfPossible
import com.botsi.view.utils.toIntList
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


internal class BotsiButtonStyleMapper {
    suspend fun map(json: JsonElement): BotsiButtonStyle {
        return withContext(Dispatchers.Default) {
            with(json.asJsonObject) {
                BotsiButtonStyle(
                    fillColor = runCatching { get("fillColor").toHexColorIfPossible() }.getOrNull(),
                    color = runCatching { get("color").asString }.getOrNull(),
                    opacity = runCatching { get("opacity").asFloat }.getOrNull(),
                    borderColor = runCatching { get("border_color").asString }.getOrNull(),
                    borderOpacity = runCatching { get("border_opacity").asFloat }.getOrNull(),
                    borderThickness = runCatching { get("border_thickness").asFloat }.getOrNull(),
                    radius = runCatching { get("radius").toIntList() }.getOrNull(),
                )
            }
        }
    }
}