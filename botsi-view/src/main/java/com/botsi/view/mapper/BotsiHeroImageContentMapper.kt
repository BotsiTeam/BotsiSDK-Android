package com.botsi.view.mapper

import com.botsi.view.model.content.BotsiContentStyle
import com.botsi.view.model.content.BotsiHeroImageContent
import com.botsi.view.model.content.BotsiHeroLayout
import com.botsi.view.model.content.BotsiShape
import com.botsi.view.model.content.BotsiTint
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


internal class BotsiHeroImageContentMapper {
    suspend fun map(json: JsonElement): BotsiHeroImageContent {
        return withContext(Dispatchers.Default) {
            with(json.asJsonObject) {
                BotsiHeroImageContent(
                    type = runCatching { get("type").asString }.getOrNull(),
                    style = runCatching {
                        BotsiContentStyle.valueOf(get("style").asString.capitalize())
                    }.getOrDefault(BotsiContentStyle.Overlay),
                    height = runCatching { get("height").asFloat }.getOrNull(),
                    shape = runCatching {
                        BotsiShape.valueOf(get("shape").asString.capitalize())
                    }.getOrDefault(BotsiShape.Rectangle),
                    tint = runCatching { mapTint(get("tint")) }.getOrNull(),
                    layout = runCatching { mapLayout(get("layout")) }.getOrNull(),
                )
            }
        }
    }

    private fun mapTint(jsonElement: JsonElement): BotsiTint {
        return with(jsonElement.asJsonObject) {
            BotsiTint(
                opacity = runCatching { get("opacity").asFloat }.getOrNull(),
                fillColor = runCatching { get("fill_color").asString }.getOrNull()
            )
        }
    }

    private fun mapLayout(jsonElement: JsonElement): BotsiHeroLayout {
        return with(jsonElement.asJsonObject) {
            BotsiHeroLayout(
                padding = runCatching { get("padding").asString.split(" ").map { it.toInt() } }.getOrNull(),
                verticalOffset = runCatching { get("vertical_offset").asInt }.getOrNull()
            )
        }
    }

}