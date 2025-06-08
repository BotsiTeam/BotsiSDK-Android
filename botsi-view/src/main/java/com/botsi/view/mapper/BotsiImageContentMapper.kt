package com.botsi.view.mapper

import com.botsi.view.model.content.BotsiImageAspect
import com.botsi.view.model.content.BotsiImageContent
import com.botsi.view.model.content.BotsiProductsContent
import com.botsi.view.utils.toCapitalizedString
import com.botsi.view.utils.toIntList
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


internal class BotsiImageContentMapper {
    suspend fun map(json: JsonElement): BotsiImageContent {
        return withContext(Dispatchers.Default) {
            with(json.asJsonObject) {
                BotsiImageContent(
                    image = runCatching { get("image").asString }.getOrNull(),
                    height = runCatching { get("height").asInt }.getOrNull(),
                    verticalOffset = runCatching { get("vertical_offset").asInt }.getOrNull(),
                    aspect = runCatching { BotsiImageAspect.valueOf(get("aspect").toCapitalizedString()) }.getOrNull(),
                    padding = runCatching { get("padding").toIntList() }.getOrNull(),
                )
            }
        }
    }
}