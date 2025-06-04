package com.botsi.view.mapper

import com.botsi.view.model.content.BotsiOnOverflowBehavior
import com.botsi.view.model.content.BotsiTextContent
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


internal class BotsiTextContentMapper(
    private val textMapper: BotsiTextMapper,
) {
    suspend fun map(json: JsonElement): BotsiTextContent {
        return withContext(Dispatchers.Default) {
            with(json.asJsonObject) {
                BotsiTextContent(
                    text = runCatching { textMapper.map(get("text")) }.getOrNull(),
                    maxLines = runCatching { get("max_lines").asInt }.getOrNull(),
                    onOverflow = runCatching {
                        BotsiOnOverflowBehavior.valueOf(get("on_overflow").asString.capitalize())
                    }.getOrNull(),
                    margin = runCatching { get("margin").asString.split(" ").map { it.toInt() } }.getOrNull(),
                    verticalOffset = runCatching { get("vertical_offset").asInt }.getOrNull(),
                )
            }
        }
    }
}