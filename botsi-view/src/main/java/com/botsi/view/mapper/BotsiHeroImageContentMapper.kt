package com.botsi.view.mapper

import com.botsi.view.model.content.BotsiHeroImageContentStyle
import com.botsi.view.model.content.BotsiHeroImageContent
import com.botsi.view.model.content.BotsiHeroLayout
import com.botsi.view.model.content.BotsiHeroImageShape
import com.botsi.view.model.content.BotsiTint
import com.botsi.view.utils.toCapitalizedString
import com.botsi.view.utils.toHexColorIfPossible
import com.botsi.view.utils.toFillBehaviourIfPossible
import com.botsi.view.utils.toIntList
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


internal class BotsiHeroImageContentMapper {
    suspend fun map(json: JsonElement): BotsiHeroImageContent {
        return withContext(Dispatchers.Default) {
            with(json.asJsonObject) {
                BotsiHeroImageContent(
                    type = runCatching { get("type").asString }.getOrNull(),
                    style = runCatching {
                        BotsiHeroImageContentStyle.valueOf(get("style").toCapitalizedString())
                    }.getOrDefault(BotsiHeroImageContentStyle.Overlay),
                    height = runCatching { get("height").asFloat }.getOrNull(),
                    backgroundImage = runCatching { get("background_image").asString }.getOrNull(),
                    shape = runCatching {
                        BotsiHeroImageShape.findByKey(get("shape").asString)
                    }.getOrDefault(BotsiHeroImageShape.Rectangle),
                    tint = runCatching { mapTint(get("tint")) }.getOrNull(),
                    layout = runCatching { mapLayout(get("layout")) }.getOrNull(),
                )
            }
        }
    }

    private fun mapTint(jsonElement: JsonElement): BotsiTint {
        return with(jsonElement.asJsonObject) {
            BotsiTint(
                opacity = runCatching { get("opacity").asFloat }.getOrNull(),
                fillColor = runCatching { (get("color") ?: get("fill_color") ?: get("fillColor")).toFillBehaviourIfPossible() }.getOrNull()
            )
        }
    }

    private fun mapLayout(jsonElement: JsonElement): BotsiHeroLayout {
        return with(jsonElement.asJsonObject) {
            BotsiHeroLayout(
                padding = runCatching { get("padding").toIntList() }.getOrNull(),
                verticalOffset = runCatching { get("vertical_offset").asInt }.getOrNull()
            )
        }
    }

}
