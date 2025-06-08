package com.botsi.view.mapper

import com.botsi.view.model.content.BotsiAlign
import com.botsi.view.model.content.BotsiDefaultIcon
import com.botsi.view.model.content.BotsiListContent
import com.botsi.view.utils.toCapitalizedString
import com.botsi.view.utils.toIntList
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


internal class BotsiListContentMapper(
    private val textMapper: BotsiTextMapper,
) {
    suspend fun map(json: JsonElement): BotsiListContent {
        return withContext(Dispatchers.Default) {
            with(json.asJsonObject) {
                BotsiListContent(
                    padding = runCatching { get("padding").toIntList() }.getOrNull(),
                    verticalOffset = runCatching { get("vertical_offset").asInt }.getOrNull(),
                    itemSpacing = runCatching { get("item_spacing").asInt }.getOrNull(),
                    textSpacing = runCatching { get("text_spacing").asInt }.getOrNull(),
                    width = runCatching { get("width").asInt }.getOrNull(),
                    height = runCatching { get("height").asInt }.getOrNull(),
                    defaultIcon = runCatching { BotsiDefaultIcon.valueOf(get("default_icon").toCapitalizedString()) }.getOrNull(),
                    iconPlacement = runCatching { BotsiAlign.valueOf(get("icon_placement").toCapitalizedString()) }.getOrNull(),
                    defaultColor = runCatching { get("default_color").asString }.getOrNull(),
                    defaultOpacity = runCatching { get("default_opacity").asFloat }.getOrNull(),
                    connectorThickness = runCatching { get("connector_thickness").asInt }.getOrNull(),
                    connectorColor = runCatching { get("connector_color").asString }.getOrNull(),
                    connectorOpacity = runCatching { get("connector_opacity").asFloat }.getOrNull(),
                    titleTextStyle = runCatching { textMapper.map(get("title_text_style")) }.getOrNull(),
                    captionTextStyle = runCatching { textMapper.map(get("caption_text_style")) }.getOrNull(),
                )
            }
        }
    }
}