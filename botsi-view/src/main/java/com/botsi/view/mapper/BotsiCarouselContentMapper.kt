package com.botsi.view.mapper

import com.botsi.view.model.content.BotsiCarouselContent
import com.botsi.view.model.content.BotsiCarouselInteractive
import com.botsi.view.model.content.BotsiCarouselLastOption
import com.botsi.view.model.content.BotsiCarouselPageControlType
import com.botsi.view.model.content.BotsiCarouselStyle
import com.botsi.view.model.content.BotsiCarouselTiming
import com.botsi.view.model.content.BotsiProductsContent
import com.botsi.view.utils.toCapitalizedString
import com.botsi.view.utils.toFillBehaviourIfPossible
import com.botsi.view.utils.toHexColorIfPossible
import com.botsi.view.utils.toIntList
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


internal class BotsiCarouselContentMapper {

    suspend fun map(json: JsonElement): BotsiCarouselContent {
        return withContext(Dispatchers.Default) {
            with(json.asJsonObject) {
                BotsiCarouselContent(
                    height = runCatching { get("height").asInt }.getOrNull(),
                    pageControl = runCatching { get("page_control").asBoolean }.getOrNull(),
                    style = runCatching { mapStyle(get("style")) }.getOrNull(),
                    slideShow = runCatching { get("slide_show").asBoolean }.getOrNull(),
                    timing = runCatching { mapTiming(get("timing")) }.getOrNull(),
                    backgroundImage = runCatching { get("background_image").asString }.getOrNull(),
                    padding = runCatching { get("padding").toIntList() }.getOrNull(),
                    verticalOffset = runCatching { get("vertical_offset").asInt }.getOrNull(),
                    contentPadding = runCatching { get("content_padding").toIntList() }.getOrNull(),
                    spacing = runCatching { get("spacing").asInt }.getOrNull(),
                )
            }
        }
    }

    private fun mapStyle(jsonElement: JsonElement): BotsiCarouselStyle {
        return with(jsonElement.asJsonObject) {
            BotsiCarouselStyle(
                activeColor = runCatching {
                    (get("active_color") ?: get("activeColor")
                    ?: get("color")).toFillBehaviourIfPossible()
                }.getOrNull(),
                activeOpacity = runCatching { get("active_opacity").asFloat }.getOrNull(),
                defaultColor = runCatching {
                    (get("default_color") ?: get("defaultColor")
                    ?: get("color")).toFillBehaviourIfPossible()
                }.getOrNull(),
                defaultOpacity = runCatching { get("default_opacity").asFloat }.getOrNull(),
                size = runCatching { get("size").asInt }.getOrNull(),
                sizeOption = runCatching {
                    BotsiCarouselPageControlType.valueOf(get("size_option").toCapitalizedString())
                }.getOrNull(),
                padding = runCatching { get("padding").toIntList() }.getOrNull(),
                spacing = runCatching { get("spacing").asInt }.getOrNull(),
            )
        }
    }

    private fun mapTiming(jsonElement: JsonElement): BotsiCarouselTiming {
        return with(jsonElement.asJsonObject) {
            BotsiCarouselTiming(
                timing = runCatching { get("timing").asLong }.getOrNull(),
                initialTiming = runCatching { get("initial_timing").asLong }.getOrNull(),
                lastOption = runCatching { BotsiCarouselLastOption.findByKey(get("last_option").asString) }.getOrNull(),
                transition = runCatching { get("transition").asLong }.getOrNull(),
                interactive = runCatching { BotsiCarouselInteractive.findByKey(get("interactive").asString) }.getOrNull(),
            )
        }
    }
}
