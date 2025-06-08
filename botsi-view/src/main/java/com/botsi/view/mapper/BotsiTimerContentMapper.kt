package com.botsi.view.mapper

import com.botsi.view.model.content.BotsiTimerContent
import com.botsi.view.model.content.BotsiTimerFormat
import com.botsi.view.model.content.BotsiTimerSeparator
import com.botsi.view.utils.toCapitalizedString
import com.botsi.view.utils.toIntList
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


internal class BotsiTimerContentMapper(
    private val textMapper: BotsiTextMapper,
) {
    suspend fun map(json: JsonElement): BotsiTimerContent {
        return withContext(Dispatchers.Default) {
            with(json.asJsonObject) {
                BotsiTimerContent(
                    format = runCatching { BotsiTimerFormat.findByFormat(get("format").asString) }.getOrNull(),
                    separator = runCatching { BotsiTimerSeparator.valueOf(get("separator").toCapitalizedString()) }.getOrNull(),
                    startText = runCatching { get("start_text").asString }.getOrNull(),
                    beforeText = runCatching { get("before_text").asString }.getOrNull(),
                    afterText = runCatching { get("after_text").asString }.getOrNull(),
                    beforeTextFallback = runCatching { get("before_text_fallback").asString }.getOrNull(),
                    afterTextFallback = runCatching { get("after_text_fallback").asString }.getOrNull(),
                    style = runCatching { textMapper.map(get("style")) }.getOrNull(),
                    padding = runCatching { get("padding").toIntList() }.getOrNull(),
                    verticalOffset = runCatching { get("vertical_offset").asInt }.getOrNull(),
                )
            }
        }
    }
}