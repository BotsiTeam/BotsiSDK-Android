package com.botsi.view.mapper

import com.botsi.view.model.content.BotsiLayoutDirection
import com.botsi.view.model.content.BotsiLinksContent
import com.botsi.view.model.content.BotsiLinksContentLayout
import com.botsi.view.model.content.BotsiLinksStyle
import com.botsi.view.model.content.BotsiLinksText
import com.botsi.view.utils.toCapitalizedString
import com.botsi.view.utils.toHexColorIfPossible
import com.botsi.view.utils.toIntList
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class BotsiLinksContentMapper(
    private val fontMapper: BotsiFontMapper,
) {
    suspend fun map(jsonElement: JsonElement): BotsiLinksContent {
        return withContext(Dispatchers.Default) {
            with(jsonElement.asJsonObject) {
                BotsiLinksContent(
                    hasTermOfService = runCatching { get("has_term_of_service").asBoolean }.getOrNull(),
                    hasPrivacyPolicy = runCatching { get("has_privacy_policy").asBoolean }.getOrNull(),
                    hasRestoreButton = runCatching { get("has_restore_button").asBoolean }.getOrNull(),
                    hasLoginButton = runCatching { get("has_login_button").asBoolean }.getOrNull(),
                    termOfService = runCatching { mapLinksText(get("term_of_service")) }.getOrNull(),
                    privacyPolicy = runCatching { mapLinksText(get("privacy_policy")) }.getOrNull(),
                    restoreButton = runCatching { mapLinksText(get("restore_button")) }.getOrNull(),
                    loginButton = runCatching { mapLinksText(get("login_button")) }.getOrNull(),
                    style = runCatching { mapLinksStyle(get("style")) }.getOrNull(),
                    padding = runCatching { get("padding").toIntList() }.getOrNull(),
                    verticalOffset = runCatching { get("vertical_offset").asInt }.getOrNull(),
                    contentLayout = runCatching { mapContentLayout(get("content_layout")) }.getOrNull(),
                )
            }
        }
    }

    private fun mapLinksText(jsonElement: JsonElement): BotsiLinksText {
        return with(jsonElement.asJsonObject) {
            BotsiLinksText(
                text = runCatching { get("text").asString }.getOrNull(),
                textFallback = runCatching { get("text_fallback").asString }.getOrNull(),
                url = runCatching { get("url").asString }.getOrNull(),
                urlFallback = runCatching { get("url_fallback").asString }.getOrNull(),
            )
        }
    }

    private suspend fun mapLinksStyle(jsonElement: JsonElement): BotsiLinksStyle {
        return with(jsonElement.asJsonObject) {
            BotsiLinksStyle(
                font = runCatching { fontMapper.map(get("font")) }.getOrNull(),
                size = runCatching { get("size").asFloat }.getOrNull(),
                color = runCatching { (get("color") ?: get("fill_color") ?: get("fillColor")).toHexColorIfPossible() }.getOrNull(),
                opacity = runCatching { get("opacity").asFloat }.getOrNull(),
                dividersColor = runCatching { (get("dividers_color") ?: get("dividersColor") ?: get("color")).toHexColorIfPossible() }.getOrNull(),
                dividersOpacity = runCatching { get("dividers_opacity").asFloat }.getOrNull(),
                dividersThickness = runCatching { get("dividers_thickness").asInt }.getOrNull(),
            )
        }
    }

    private fun mapContentLayout(jsonElement: JsonElement): BotsiLinksContentLayout {
        return with(jsonElement.asJsonObject) {
            BotsiLinksContentLayout(
                spacing = runCatching { get("spacing").asInt }.getOrNull(),
                layout = runCatching { BotsiLayoutDirection.valueOf(get("layout").toCapitalizedString()) }.getOrNull(),
            )
        }
    }
}
