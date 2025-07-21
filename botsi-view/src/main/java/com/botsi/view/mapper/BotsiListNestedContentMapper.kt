package com.botsi.view.mapper

import androidx.annotation.RestrictTo
import com.botsi.view.model.content.BotsiListNestedContent
import com.botsi.view.utils.toHexColorIfPossible
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal class BotsiListNestedContentMapper(
    private val textMapper: BotsiTextMapper,
) {

    suspend fun map(json: JsonElement): BotsiListNestedContent {
        return withContext(Dispatchers.Default) {
            with(json.asJsonObject) {
                BotsiListNestedContent(
                    icon = runCatching { get("icon").asString }.getOrNull(),
                    connectorThickness = runCatching { get("connector_thickness").asInt }.getOrNull(),
                    connectorColor = runCatching { (get("connector_color") ?: get("connectorColor") ?: get("color")).toHexColorIfPossible() }.getOrNull(),
                    connectorOpacity = runCatching { get("connector_opacity").asInt }.getOrNull(),
                    titleText = runCatching { get("title_text").asString }.getOrNull(),
                    titleTextFallback = runCatching { get("title_text_fallback").asString }.getOrNull(),
                    titleTextStyle = runCatching { textMapper.map(get("title_text_style")) }.getOrNull(),
                    captionText = runCatching { get("caption_text").asString }.getOrNull(),
                    captionTextFallback = runCatching { get("caption_text_fallback").asString }.getOrNull(),
                    captionTextStyle = runCatching { textMapper.map(get("caption_text_style")) }.getOrNull(),
                )
            }
        }
    }
}
