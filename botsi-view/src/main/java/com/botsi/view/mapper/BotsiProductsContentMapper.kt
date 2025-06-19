package com.botsi.view.mapper

import com.botsi.view.model.content.BotsiAlign
import com.botsi.view.model.content.BotsiProductContentLayout
import com.botsi.view.model.content.BotsiProductStyle
import com.botsi.view.model.content.BotsiProductTextStyle
import com.botsi.view.model.content.BotsiProductsContent
import com.botsi.view.utils.toCapitalizedString
import com.botsi.view.utils.toIntList
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


internal class BotsiProductsContentMapper(private val fontMapper: BotsiFontMapper) {
    suspend fun map(json: JsonElement): BotsiProductsContent {
        return withContext(Dispatchers.Default) {
            with(json.asJsonObject) {
                BotsiProductsContent(
                    grouping = runCatching { get("grouping").asString }.getOrNull(),
                    selectedProduct = runCatching { get("selected_product").asString }.getOrNull(),
                    state = runCatching { get("state").asString }.getOrNull(),
                    defaultStyle = runCatching { mapStyle(get("default_style")) }.getOrNull(),
                    selectedStyle = runCatching { mapStyle(get("")) }.getOrNull(),
                    text1 = runCatching { mapText(get("text1")) }.getOrNull(),
                    text2 = runCatching { mapText(get("text2")) }.getOrNull(),
                    text3 = runCatching { mapText(get("text3")) }.getOrNull(),
                    text4 = runCatching { mapText(get("text4")) }.getOrNull(),
                    padding = runCatching { get("padding").toIntList() }.getOrNull(),
                    verticalOffset = runCatching { get("vertical_offset").asInt }.getOrNull(),
                    contentLayout = runCatching { mapContentLayout(get("content_layout")) }.getOrNull(),
                )
            }
        }
    }

    private fun mapStyle(jsonElement: JsonElement): BotsiProductStyle {
        return with(jsonElement.asJsonObject) {
            BotsiProductStyle(
                borderColor = runCatching { get("border_color").asString }.getOrNull(),
                borderOpacity = runCatching { get("border_opacity").asFloat }.getOrNull(),
                borderThickness = runCatching { get("border_thickness").asInt }.getOrNull(),
                color = runCatching { get("color").asString }.getOrNull(),
                opacity = runCatching { get("opacity").asFloat }.getOrNull(),
                radius = runCatching { get("radius").asString }.getOrNull(),
            )
        }
    }

    private fun mapContentLayout(jsonElement: JsonElement): BotsiProductContentLayout {
        return with(jsonElement.asJsonObject) {
            BotsiProductContentLayout(
                layout = runCatching { get("layout").asString }.getOrNull(),
                align = runCatching { BotsiAlign.valueOf(get("align").toCapitalizedString()) }.getOrNull(),
                padding = runCatching { get("padding").toIntList() }.getOrNull(),
                spacing = runCatching { get("spacing").asInt }.getOrNull(),
            )
        }
    }

    private suspend fun mapText(jsonElement: JsonElement): BotsiProductTextStyle {
        return with(jsonElement.asJsonObject) {
            BotsiProductTextStyle(
                font = runCatching { fontMapper.map(get("font")) }.getOrNull(),
                size = runCatching { get("size").asInt }.getOrNull(),
                color = runCatching { get("color").asString }.getOrNull(),
                opacity = runCatching { get("opacity").asFloat }.getOrNull(),
                selectedColor = runCatching { get("selected_color").asString }.getOrNull(),
                selectedOpacity = runCatching { get("selected_opacity").asFloat }.getOrNull(),
            )
        }
    }
}