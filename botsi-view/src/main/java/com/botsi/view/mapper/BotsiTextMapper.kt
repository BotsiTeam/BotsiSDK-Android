package com.botsi.view.mapper

import com.botsi.view.model.content.BotsiAlign
import com.botsi.view.model.content.BotsiText
import com.botsi.view.utils.toCapitalizedString
import com.botsi.view.utils.toHexColorIfPossible
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


internal class BotsiTextMapper(
    private val fontMapper: BotsiFontMapper,
) {
    suspend fun map(json: JsonElement): BotsiText {
        return withContext(Dispatchers.Default) {
            with(json.asJsonObject) {
                BotsiText(
                    text = runCatching { get("text").asString }.getOrNull(),
                    font = runCatching { fontMapper.map(get("font")) }.getOrNull(),
                    size = runCatching { get("size").asFloat }.getOrNull(),
                    color = runCatching { (get("color") ?: get("fill_color") ?: get("fillColor")).toHexColorIfPossible() }.getOrNull(),
                    opacity = runCatching { get("opacity").asFloat }.getOrNull(),
                    align = runCatching {
                        BotsiAlign.valueOf(get("align").toCapitalizedString())
                    }.getOrNull(),
                )
            }
        }
    }
}
