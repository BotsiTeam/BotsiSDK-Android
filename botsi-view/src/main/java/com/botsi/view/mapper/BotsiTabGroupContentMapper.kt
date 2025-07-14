package com.botsi.view.mapper

import com.botsi.view.model.content.BotsiTabGroupContent
import com.botsi.view.model.content.BotsiProductContentLayout
import com.botsi.view.model.content.BotsiLayoutDirection
import com.botsi.view.model.content.BotsiAlign
import com.botsi.view.utils.toCapitalizedString
import com.botsi.view.utils.toIntList
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class BotsiTabGroupContentMapper {
    suspend fun map(json: JsonElement): BotsiTabGroupContent {
        return withContext(Dispatchers.Default) {
            with(json.asJsonObject) {
                BotsiTabGroupContent(
                    tabTitle = runCatching { get("tab_title").asString }.getOrNull(),
                    textFallback = runCatching { get("text_fallback").asString }.getOrNull(),
                    selectedProduct = runCatching { get("selected_product").asString }.getOrNull(),
                    padding = runCatching { get("padding").toIntList() }.getOrNull(),
                    verticalOffset = runCatching { get("vertical_offset").asInt }.getOrNull(),
                    contentLayout = runCatching { mapContentLayout(get("content_layout")) }.getOrNull()
                )
            }
        }
    }

    private fun mapContentLayout(jsonElement: JsonElement): BotsiProductContentLayout {
        return with(jsonElement.asJsonObject) {
            BotsiProductContentLayout(
                layout = runCatching { BotsiLayoutDirection.valueOf(get("layout").toCapitalizedString()) }.getOrNull(),
                align = runCatching { BotsiAlign.valueOf(get("align").toCapitalizedString()) }.getOrNull(),
                padding = runCatching { get("padding").toIntList() }.getOrNull(),
                spacing = runCatching { get("spacing").asInt }.getOrNull()
            )
        }
    }
}