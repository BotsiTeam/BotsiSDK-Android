package com.botsi.view.mapper

import com.botsi.view.model.content.BotsiComponentStyle
import com.botsi.view.utils.toHexColorIfPossible
import com.botsi.view.utils.toFillBehaviourIfPossible
import com.botsi.view.utils.toIntList
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


internal class BotsiComponentStyleMapper(private val fontMapper: BotsiFontMapper) {
    suspend fun map(json: JsonElement): BotsiComponentStyle {
        return withContext(Dispatchers.Default) {
            with(json.asJsonObject) {
                BotsiComponentStyle(
                    fillColor = runCatching {
                        (get("fillColor") ?: get("fill_color")
                        ?: get("color")).toFillBehaviourIfPossible()
                    }.getOrNull(),
                    color = runCatching {
                        (get("color") ?: get("fill_color")
                        ?: get("fillColor")).toHexColorIfPossible()
                    }.getOrNull(),
                    opacity = runCatching { get("opacity").asFloat }.getOrNull(),
                    borderColor = runCatching {
                        (get("border_color") ?: get("borderColor")
                        ?: get("color")).toHexColorIfPossible()
                    }.getOrNull(),
                    borderOpacity = runCatching { get("border_opacity").asFloat }.getOrNull(),
                    borderThickness = runCatching { get("border_thickness").asFloat }.getOrNull(),
                    radius = runCatching { get("radius").toIntList() }.getOrNull(),
                    size = runCatching { get("size").asFloat }.getOrNull(),
                    font = runCatching { fontMapper.map(get("font")) }.getOrNull()
                )
            }
        }
    }
}
