package com.botsi.view.mapper

import com.botsi.view.model.content.BotsiFooterContent
import com.botsi.view.model.content.BotsiFooterStyle
import com.botsi.view.utils.toHexColorIfPossible
import com.botsi.view.utils.toIntList
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


internal class BotsiFooterContentMapper {
    suspend fun map(json: JsonElement): BotsiFooterContent {
        return withContext(Dispatchers.Default) {
            with(json.asJsonObject) {
                BotsiFooterContent(
                    padding = runCatching { get("padding").toIntList() }.getOrNull(),
                    spacing = runCatching { get("spacing").asInt }.getOrNull(),
                    style = runCatching { mapStyle(get("style")) }.getOrNull(),
                )
            }
        }
    }

    private fun mapStyle(jsonElement: JsonElement): BotsiFooterStyle {
        return with(jsonElement.asJsonObject) {
            BotsiFooterStyle(
                borderColor = runCatching { (get("border_color") ?: get("borderColor") ?: get("color")).toHexColorIfPossible() }.getOrNull(),
                borderOpacity = runCatching { get("border_opacity").asFloat }.getOrNull(),
                borderThickness = runCatching { get("border_thickness").asInt }.getOrNull(),
                color = runCatching { (get("color") ?: get("fill_color") ?: get("fillColor")).toHexColorIfPossible() }.getOrNull(),
                opacity = runCatching { get("opacity").asFloat }.getOrNull(),
                radius = runCatching { get("radius").toIntList() }.getOrNull(),
            )
        }
    }

}
