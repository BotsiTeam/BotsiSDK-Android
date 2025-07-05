package com.botsi.view.mapper

import com.botsi.view.model.content.BotsiAlign
import com.botsi.view.model.content.BotsiLayoutDirection
import com.botsi.view.model.content.BotsiProductToggleStateContent
import com.botsi.view.model.content.BotsiProductToggleStateContentLayout
import com.botsi.view.utils.toCapitalizedString
import com.botsi.view.utils.toIntList
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


internal class BotsiProductToggleStateContentMapper {
    suspend fun map(json: JsonElement): BotsiProductToggleStateContent {
        return withContext(Dispatchers.Default) {
            with(json.asJsonObject) {
                BotsiProductToggleStateContent(
                    padding = runCatching { get("padding").toIntList() }.getOrNull(),
                    verticalOffset = runCatching { get("vertical_offset").asInt }.getOrNull(),
                    contentLayout = runCatching { mapContentLayout(get("content_layout")) }.getOrNull()
                )
            }
        }
    }

    private fun mapContentLayout(jsonElement: JsonElement): BotsiProductToggleStateContentLayout {
        return with(jsonElement.asJsonObject) {
            BotsiProductToggleStateContentLayout(
                layout = runCatching { 
                    BotsiLayoutDirection.valueOf(get("layout").toCapitalizedString()) 
                }.getOrNull(),
                align = runCatching { 
                    BotsiAlign.valueOf(get("align").toCapitalizedString()) 
                }.getOrNull(),
                padding = runCatching { get("padding").toIntList() }.getOrNull(),
                spacing = runCatching { get("spacing").asInt }.getOrNull()
            )
        }
    }
}
