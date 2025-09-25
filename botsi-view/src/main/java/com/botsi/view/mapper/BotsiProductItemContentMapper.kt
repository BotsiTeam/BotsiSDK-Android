package com.botsi.view.mapper

import com.botsi.view.mapper.BotsiFontMapper
import com.botsi.view.model.content.BotsiBadge
import com.botsi.view.model.content.BotsiProductItemContent
import com.botsi.view.model.content.BotsiProductState
import com.botsi.view.model.content.BotsiProductStateStyle
import com.botsi.view.model.content.BotsiProductStyle
import com.botsi.view.model.content.BotsiProductText
import com.botsi.view.model.content.BotsiProductTextStyle
import com.botsi.view.utils.toCapitalizedString
import com.botsi.view.utils.toFillBehaviourIfPossible
import com.botsi.view.utils.toHexColorIfPossible
import com.botsi.view.utils.toIntList
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


internal class BotsiProductItemContentMapper(private val fontMapper: BotsiFontMapper) {
    suspend fun map(json: JsonElement): BotsiProductItemContent {
        return withContext(Dispatchers.Default) {
            with(json.asJsonObject) {
                BotsiProductItemContent(
                    state = runCatching { BotsiProductState.valueOf(get("state").toCapitalizedString()) }.getOrNull(),
                    offerState = runCatching { get("offer_state").asString }.getOrNull(),
                    defaultText = runCatching { mapProductText(get("default_text")) }.getOrNull(),
                    freeText = runCatching { mapProductText(get("free_text")) }.getOrNull(),
                    paygText = runCatching { mapProductText(get("payg_text")) }.getOrNull(),
                    paufText = runCatching { mapProductText(get("pauf_text")) }.getOrNull(),
                    defaultState = runCatching { mapProductStateStyle(get("default_state")) }.getOrNull(),
                    selectedState = runCatching { mapProductStateStyle(get("selected_state")) }.getOrNull(),
                    defaultStyle = runCatching { mapStyle(get("default_style")) }.getOrNull(),
                    selectedStyle = runCatching { mapStyle(get("selected_style")) }.getOrNull(),
                    isBadge = runCatching { get("is_badge").asBoolean }.getOrNull(),
                    badge = runCatching { mapBadge(get("badge")) }.getOrNull()
                )
            }
        }
    }

    private fun mapProductText(jsonElement: JsonElement): BotsiProductText {
        return with(jsonElement.asJsonObject) {
            BotsiProductText(
                text1 = runCatching { get("text_1").asString }.getOrNull(),
                text1Fallback = runCatching { get("text_1_fallback").asString }.getOrNull(),
                text2 = runCatching { get("text_2").asString }.getOrNull(),
                text2Fallback = runCatching { get("text_2_fallback").asString }.getOrNull(),
                text3 = runCatching { get("text_3").asString }.getOrNull(),
                text3Fallback = runCatching { get("text_3_fallback").asString }.getOrNull(),
                text4 = runCatching { get("text_4").asString }.getOrNull(),
                text4Fallback = runCatching { get("text_4_fallback").asString }.getOrNull()
            )
        }
    }

    private suspend fun mapProductStateStyle(jsonElement: JsonElement): BotsiProductStateStyle {
        return with(jsonElement.asJsonObject) {
            BotsiProductStateStyle(
                text1 = runCatching { mapText(get("text_1")) }.getOrNull(),
                text2 = runCatching { mapText(get("text_2")) }.getOrNull(),
                text3 = runCatching { mapText(get("text_3")) }.getOrNull(),
                text4 = runCatching { mapText(get("text_4")) }.getOrNull()
            )
        }
    }

    private fun mapStyle(jsonElement: JsonElement): BotsiProductStyle {
        return with(jsonElement.asJsonObject) {
            BotsiProductStyle(
                borderColor = runCatching { (get("border_color") ?: get("borderColor") ?: get("color")).toHexColorIfPossible() }.getOrNull(),
                borderOpacity = runCatching { get("border_opacity").asFloat }.getOrNull(),
                borderThickness = runCatching { get("border_thickness").asInt }.getOrNull(),
                color = runCatching { (get("color") ?: get("fill_color") ?: get("fillColor")).toFillBehaviourIfPossible() }.getOrNull(),
                opacity = runCatching { get("opacity").asFloat }.getOrNull(),
                radius = runCatching { get("radius").toIntList() }.getOrNull(),
            )
        }
    }

    private suspend fun mapText(jsonElement: JsonElement): BotsiProductTextStyle {
        return with(jsonElement.asJsonObject) {
            BotsiProductTextStyle(
                font = runCatching { fontMapper.map(get("font")) }.getOrNull(),
                size = runCatching { get("size").asInt }.getOrNull(),
                color = runCatching { (get("color") ?: get("fill_color") ?: get("fillColor")).toHexColorIfPossible() }.getOrNull(),
                opacity = runCatching { get("opacity").asFloat }.getOrNull(),
                selectedColor = runCatching { (get("selected_color") ?: get("selectedColor") ?: get("color")).toHexColorIfPossible() }.getOrNull(),
                selectedOpacity = runCatching { get("selected_opacity").asFloat }.getOrNull(),
            )
        }
    }

    private suspend fun mapBadge(jsonElement: JsonElement): BotsiBadge {
        return with(jsonElement.asJsonObject) {
            BotsiBadge(
                badgeText = runCatching { get("badge_text").asString }.getOrNull(),
                badgeColor = runCatching { (get("badge_color") ?: get("badgeColor") ?: get("color")).toFillBehaviourIfPossible() }.getOrNull(),
                badgeOpacity = runCatching { get("badge_opacity").asFloat }.getOrNull(),
                badgeRadius = runCatching { get("badge_radius").toIntList() }.getOrNull(),
                badgeTextFont = runCatching { fontMapper.map(get("badge_text_font")) }.getOrNull(),
                badgeTextSize = runCatching { get("badge_text_size").asInt }.getOrNull(),
                badgeTextColor = runCatching { (get("badge_text_color") ?: get("badgeTextColor") ?: get("color")).toHexColorIfPossible() }.getOrNull(),
                badgeTextOpacity = runCatching { get("badge_text_opacity").asFloat }.getOrNull()
            )
        }
    }
}
