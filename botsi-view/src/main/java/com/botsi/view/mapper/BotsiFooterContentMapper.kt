package com.botsi.view.mapper

import com.botsi.view.model.content.BotsiFooterContent
import com.botsi.view.model.content.BotsiFooterStyle
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


internal class BotsiFooterContentMapper {
    suspend fun map(json: JsonElement): BotsiFooterContent {
        return withContext(Dispatchers.Default) {
            with(json.asJsonObject) {
                BotsiFooterContent(
                    padding = runCatching {
                        get("padding").asString.split(" ").map { it.toInt() }
                    }.getOrNull(),
                    spacing = runCatching { get("spacing").asInt }.getOrNull(),
                    style = runCatching { mapStyle(get("style")) }.getOrNull(),
                )
            }
        }
    }

    private fun mapStyle(jsonElement: JsonElement): BotsiFooterStyle {
        return with(jsonElement.asJsonObject) {
            BotsiFooterStyle(
                borderColor = runCatching { get("border_color").asString }.getOrNull(),
                borderOpacity = runCatching { get("border_opacity").asFloat }.getOrNull(),
                borderThickness = runCatching { get("border_thickness").asInt }.getOrNull(),
                color = runCatching { get("color").asString }.getOrNull(),
                opacity = runCatching { get("opacity").asFloat }.getOrNull(),
                radius = runCatching { get("radius").asString.split(" ").map { it.toInt() } }.getOrNull(),
            )
        }
    }

}