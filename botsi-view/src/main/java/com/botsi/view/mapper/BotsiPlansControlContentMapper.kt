package com.botsi.view.mapper

import com.botsi.view.model.content.BotsiPlansControlContent
import com.botsi.view.model.content.BotsiPlansControlText
import com.botsi.view.model.content.BotsiPlansControlContentLayout
import com.botsi.view.model.content.BotsiAlign
import com.botsi.view.utils.toCapitalizedString
import com.botsi.view.utils.toIntList
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class BotsiPlansControlContentMapper(
    private val textMapper: BotsiTextMapper,
    private val buttonStyleMapper: BotsiComponentStyleMapper
) {
    suspend fun map(json: JsonElement): BotsiPlansControlContent {
        return withContext(Dispatchers.Default) {
            with(json.asJsonObject) {
                BotsiPlansControlContent(
                    state = runCatching { get("state").asString == "expanded" }.getOrNull(),
                    defaultText = runCatching { mapPlansControlText(get("default_text")) }.getOrNull(),
                    morePlansShownText = runCatching { mapPlansControlText(get("more_plans_shown_text")) }.getOrNull(),
                    style = runCatching { buttonStyleMapper.map(get("style")) }.getOrNull(),
                    margin = runCatching { get("margin").toIntList() }.getOrNull(),
                    verticalOffset = runCatching { get("vertical_offset").asInt }.getOrNull(),
                    contentLayout = runCatching { mapContentLayout(get("content_layout")) }.getOrNull()
                )
            }
        }
    }

    private suspend fun mapPlansControlText(jsonElement: JsonElement): BotsiPlansControlText {
        return with(jsonElement.asJsonObject) {
            BotsiPlansControlText(
                text = runCatching { get("text").asString }.getOrNull(),
                textFallback = runCatching { get("text_fallback").asString }.getOrNull(),
                textStyle = runCatching { textMapper.map(get("text_style")) }.getOrNull(),
                secondaryText = runCatching { get("secondary_text").asString }.getOrNull(),
                secondaryTextFallback = runCatching { get("secondary_text_fallback").asString }.getOrNull(),
                secondaryTextStyle = runCatching { textMapper.map(get("secondary_text_style")) }.getOrNull()
            )
        }
    }

    private fun mapContentLayout(jsonElement: JsonElement): BotsiPlansControlContentLayout {
        return with(jsonElement.asJsonObject) {
            BotsiPlansControlContentLayout(
                align = runCatching { BotsiAlign.valueOf(get("align").toCapitalizedString()) }.getOrNull(),
                padding = runCatching { get("padding").toIntList() }.getOrNull()
            )
        }
    }
}