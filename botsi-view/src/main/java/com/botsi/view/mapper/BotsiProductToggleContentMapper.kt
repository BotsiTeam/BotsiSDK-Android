package com.botsi.view.mapper

import com.botsi.view.model.content.BotsiProductToggleContent
import com.botsi.view.model.content.BotsiProductToggleContentLayout
import com.botsi.view.model.content.BotsiProductToggleState
import com.botsi.view.model.content.BotsiProductToggleStyle
import com.botsi.view.utils.toFillBehaviourIfPossible
import com.botsi.view.utils.toHexColorIfPossible
import com.botsi.view.utils.toIntList
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


internal class BotsiProductToggleContentMapper(
    private val textMapper: BotsiTextMapper,
) {
    suspend fun map(json: JsonElement): BotsiProductToggleContent {
        return withContext(Dispatchers.Default) {
            with(json.asJsonObject) {
                BotsiProductToggleContent(
                    state = runCatching { get("state").asBoolean }.getOrNull(),
                    toggleColor = runCatching { (get("toggle_color") ?: get("toggleColor") ?: get("color")).toHexColorIfPossible() }.getOrNull(),
                    toggleOpacity = runCatching { get("toggle_opacity").asFloat }.getOrNull(),
                    toggleStyle = runCatching { mapToggleStyle(get("toggle_style")) }.getOrNull(),
                    activeState = runCatching { mapToggleState(get("active_state")) }.getOrNull(),
                    inactiveState = runCatching { mapToggleState(get("inactive_state")) }.getOrNull(),
                    padding = runCatching { get("padding").toIntList() }.getOrNull(),
                    verticaOffset = runCatching { get("vertical_offset").asInt }.getOrNull(),
                    contentLayout = runCatching { mapContentLayout(get("content_layout")) }.getOrNull()
                )
            }
        }
    }

    private fun mapToggleStyle(jsonElement: JsonElement): BotsiProductToggleStyle {
        return with(jsonElement.asJsonObject) {
            BotsiProductToggleStyle(
                color = runCatching { (get("color") ?: get("fill_color") ?: get("fillColor")).toFillBehaviourIfPossible() }.getOrNull(),
                opacity = runCatching { get("opacity").asFloat }.getOrNull(),
                borderColor = runCatching { (get("border_color") ?: get("borderColor") ?: get("color")).toHexColorIfPossible() }.getOrNull(),
                borderOpacity = runCatching { get("border_opacity").asFloat }.getOrNull(),
                borderThickness = runCatching { get("border_thickness").asInt }.getOrNull(),
                radius = runCatching { get("radius").toIntList() }.getOrNull()
            )
        }
    }

    private suspend fun mapToggleState(jsonElement: JsonElement): BotsiProductToggleState {
        return with(jsonElement.asJsonObject) {
            BotsiProductToggleState(
                text = runCatching { get("text").asString }.getOrNull(),
                textStyle = runCatching { textMapper.map(get("text_style")) }.getOrNull(),
                secondaryText = runCatching { get("secondary_text").asString }.getOrNull(),
                secondaryTextStyle = runCatching { textMapper.map(get("secondary_text_style")) }.getOrNull()
            )
        }
    }

    private fun mapContentLayout(jsonElement: JsonElement): BotsiProductToggleContentLayout {
        return with(jsonElement.asJsonObject) {
            BotsiProductToggleContentLayout(
                padding = runCatching { get("padding").toIntList() }.getOrNull(),
                spacing = runCatching { get("spacing").asInt }.getOrNull()
            )
        }
    }
}
