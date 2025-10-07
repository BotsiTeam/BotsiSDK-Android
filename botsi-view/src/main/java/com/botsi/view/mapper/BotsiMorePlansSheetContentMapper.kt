package com.botsi.view.mapper

import com.botsi.view.model.content.BotsiAlign
import com.botsi.view.model.content.BotsiLayoutDirection
import com.botsi.view.model.content.BotsiMorePlansSheetContent
import com.botsi.view.model.content.BotsiProductContentLayout
import com.botsi.view.utils.toCapitalizedString
import com.botsi.view.utils.toHexColorIfPossible
import com.botsi.view.utils.toIntList
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class BotsiMorePlansSheetContentMapper(
    private val textMapper: BotsiTextMapper,
    private val buttonStyleMapper: BotsiComponentStyleMapper
) {
    suspend fun map(json: JsonElement): BotsiMorePlansSheetContent {
        return withContext(Dispatchers.Default) {
            with(json.asJsonObject) {
                BotsiMorePlansSheetContent(
                    selectedProduct = runCatching { get("selected_product")?.asString }.getOrNull(),
                    padding = runCatching { get("padding")?.toIntList() }.getOrNull(),
                    verticalOffset = runCatching { get("vertical_offset")?.asInt }.getOrNull(),
                    contentLayout = runCatching { mapContentLayout(get("content_layout")) }.getOrNull(),
                    plansStyles = runCatching { buttonStyleMapper.map(get("plans_styles")) }.getOrNull(),
                    closeButtonStyles = runCatching { buttonStyleMapper.map(get("close_button_styles")) }.getOrNull(),
                    iconColor = runCatching { (get("icon_color") ?: get("iconColor") ?: get("color"))?.toHexColorIfPossible() }.getOrNull(),
                    iconOpacity = runCatching { get("icon_opacity")?.asInt }.getOrNull(),
                    iconSize = runCatching { get("icon_size")?.asString }.getOrNull(),
                    titleText = runCatching { get("title_text")?.asString }.getOrNull(),
                    secondaryText = runCatching { get("secondary_text")?.asString }.getOrNull(),
                    titleTextStyle = runCatching { textMapper.map(get("title_text_style")) }.getOrNull(),
                    secondaryTextStyle = runCatching { textMapper.map(get("secondary_text_style")) }.getOrNull()
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
                spacing = runCatching { get("spacing").asInt }.getOrNull(),
            )
        }
    }
}
