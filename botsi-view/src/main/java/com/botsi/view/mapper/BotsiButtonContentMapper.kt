package com.botsi.view.mapper

import com.botsi.view.model.content.BotsiAlign
import com.botsi.view.model.content.BotsiButtonAction
import com.botsi.view.model.content.BotsiButtonContent
import com.botsi.view.model.content.BotsiButtonContentLayout
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


internal class BotsiButtonContentMapper(
    private val buttonStyleMapper: BotsiButtonStyleMapper,
    private val textMapper: BotsiTextMapper,
) {
    suspend fun map(json: JsonElement): BotsiButtonContent {
        return withContext(Dispatchers.Default) {
            with(json.asJsonObject) {
                BotsiButtonContent(
                    action = runCatching {
                        BotsiButtonAction.valueOf(get("").asString.capitalize())
                    }.getOrNull(),
                    actionLabel = runCatching { get("").asString }.getOrNull(),
                    text = runCatching { textMapper.map(get("text")) }.getOrNull(),
                    secondaryText = runCatching { textMapper.map(get("secondary_text")) }.getOrNull(),
                    style = runCatching { buttonStyleMapper.map(get("style")) }.getOrNull(),
                    margin = runCatching { get("margin").asString.split(" ").map { it.toInt() } }.getOrNull(),
                    verticalOffset = runCatching { get("").asInt }.getOrNull(),
                    contentLayout = runCatching { mapContentLayout(get("content_layout")) }.getOrNull(),
                )
            }
        }
    }

    private fun mapContentLayout(jsonElement: JsonElement): BotsiButtonContentLayout {
        return with(jsonElement.asJsonObject) {
            BotsiButtonContentLayout(
                padding = runCatching { get("padding").asString.split(" ").map { it.toInt() } }.getOrNull(),
                align = runCatching { BotsiAlign.valueOf(get("align").asString.capitalize()) }.getOrNull()
            )
        }
    }

}