package com.botsi.view.mapper

import com.botsi.view.model.content.BotsiFont
import com.botsi.view.model.content.BotsiFontStyleType
import com.botsi.view.model.content.BotsiFontType
import com.botsi.view.utils.toCapitalizedString
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


internal class BotsiFontMapper {
    suspend fun map(json: JsonElement): BotsiFont {
        return withContext(Dispatchers.Default) {
            with(json.asJsonObject) {
                BotsiFont(
                    id = runCatching { get("id").asString }.getOrNull(),
                    name = runCatching { get("name").asString }.getOrNull(),
                    isSelected = runCatching { get("isSelected").asBoolean }.getOrNull(),
                    types = runCatching { mapFontTypes(get("types")) }.getOrNull(),
                )
            }
        }
    }

    private fun mapFontTypes(jsonElement: JsonElement): List<BotsiFontType> {
        return with(jsonElement.asJsonArray) {
            map {
                with(it.asJsonObject) {
                    BotsiFontType(
                        name = runCatching { get("name").asString }.getOrNull(),
                        id = runCatching { get("id").asString }.getOrNull(),
                        fontWeight = runCatching { get("fontWeight").asInt }.getOrNull(),
                        fontStyle = runCatching {
                            BotsiFontStyleType.valueOf(get("fontStyle").toCapitalizedString())
                        }.getOrNull(),
                        isSelected = runCatching { get("isSelected").asBoolean }.getOrNull(),
                    )
                }
            }
        }
    }
}