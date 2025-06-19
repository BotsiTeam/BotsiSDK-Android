package com.botsi.view.mapper

import com.botsi.view.model.content.BotsiAlign
import com.botsi.view.model.content.BotsiCardContent
import com.botsi.view.model.content.BotsiCardContentLayout
import com.botsi.view.model.content.BotsiCardStyle
import com.botsi.view.utils.toCapitalizedString
import com.botsi.view.utils.toIntList
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


internal class BotsiCardContentMapper {
    suspend fun map(json: JsonElement): BotsiCardContent {
        return withContext(Dispatchers.Default) {
            with(json.asJsonObject) {
                BotsiCardContent(
                    style = runCatching { mapStyle(get("style")) }.getOrNull(),
                    backgroundImage = runCatching { get("background_image").asString }.getOrNull(),
                    margin = runCatching { get("margin").toIntList() }.getOrNull(),
                    verticalOffset = runCatching { get("vertical_offset").asInt }.getOrNull(),
                    contentLayout = runCatching { mapContentLayout(get("content_layout")) }.getOrNull(),
                )
            }
        }
    }

    private fun mapContentLayout(jsonElement: JsonElement): BotsiCardContentLayout {
        return with(jsonElement.asJsonObject) {
            BotsiCardContentLayout(
                padding = runCatching { get("padding").toIntList() }.getOrNull(),
                verticalOffset = runCatching { get("vertical_offset").asInt }.getOrNull(),
                align = runCatching { BotsiAlign.valueOf(get("align").toCapitalizedString()) }.getOrNull(),
            )
        }
    }

    private fun mapStyle(jsonElement: JsonElement): BotsiCardStyle {
        return with(jsonElement.asJsonObject) {
            BotsiCardStyle(
                color = runCatching { get("color").asString }.getOrNull(),
                opacity = runCatching { get("opacity").asFloat }.getOrNull(),
                borderColor = runCatching { get("border_color").asString }.getOrNull(),
                borderOpacity = runCatching { get("border_opacity").asFloat }.getOrNull(),
                borderThickness = runCatching { get("border_thickness").asInt }.getOrNull(),
                radius = runCatching { get("radius").toIntList() }.getOrNull(),
            )
        }
    }

}