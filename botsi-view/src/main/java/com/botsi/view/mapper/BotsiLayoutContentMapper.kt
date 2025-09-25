package com.botsi.view.mapper

import androidx.annotation.RestrictTo
import com.botsi.view.model.content.BotsiAlign
import com.botsi.view.model.content.BotsiButtonAction
import com.botsi.view.model.content.BotsiButtonIconType
import com.botsi.view.model.content.BotsiButtonType
import com.botsi.view.model.content.BotsiContentLayout
import com.botsi.view.model.content.BotsiIconStyle
import com.botsi.view.model.content.BotsiLayoutContent
import com.botsi.view.model.content.BotsiTemplate
import com.botsi.view.model.content.BotsiTopButton
import com.botsi.view.utils.toCapitalizedString
import com.botsi.view.utils.toHexColorIfPossible
import com.botsi.view.utils.toFillBehaviourIfPossible
import com.botsi.view.utils.toIntList
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal class BotsiLayoutContentMapper(
    private val fontMapper: BotsiFontMapper,
    private val buttonStyleMapper: BotsiButtonStyleMapper,
    private val textMapper: BotsiTextMapper,
) {

    suspend fun map(json: JsonElement): BotsiLayoutContent {
        return withContext(Dispatchers.Default) {
            with(json.asJsonObject) {
                BotsiLayoutContent(
                    darkMode = runCatching { get("dark_mode").asBoolean }.getOrNull(),
                    purchaseFlow = runCatching { get("purchase_flow").asString }.getOrNull(),
                    fillColor = runCatching { (get("color") ?: get("fill_color") ?: get("fillColor")).toFillBehaviourIfPossible() }.getOrNull(),
                    contentLayout = runCatching { mapContentLayout(get("content_layout")) }.getOrNull(),
                    topButtons = runCatching { mapTopButtons(get("top_buttons")) }.getOrNull(),
                    defaultFont = runCatching { fontMapper.map(get("default_font")) }.getOrNull(),
                    template = runCatching { mapTemplate(get("dark_mode")) }.getOrNull(),
                )
            }
        }
    }

    private fun mapContentLayout(jsonElement: JsonElement): BotsiContentLayout {
        return with(jsonElement.asJsonObject) {
            BotsiContentLayout(
                margin = runCatching { get("margin").toIntList() }.getOrNull(),
                spacing = runCatching { get("spacing").asInt }.getOrNull()
            )
        }
    }

    private fun mapTemplate(jsonElement: JsonElement): BotsiTemplate {
        return with(jsonElement.asJsonObject) {
            BotsiTemplate(
                image = runCatching { get("image").asString }.getOrNull(),
                name = runCatching { get("name").asString }.getOrNull(),
                id = runCatching { get("id").asString }.getOrNull(),
            )
        }
    }

    private suspend fun mapTopButtons(jsonElement: JsonElement): List<BotsiTopButton> {
        return with(jsonElement.asJsonArray) {
            map { item ->
                with(item.asJsonObject) {
                    BotsiTopButton(
                        action = runCatching {
                            BotsiButtonAction.valueOf(get("action").toCapitalizedString())
                        }.getOrDefault(BotsiButtonAction.None),
                        enabled = runCatching { get("enabled").asBoolean }.getOrNull(),
                        actionId = runCatching { get("actionId").asString }.getOrNull(),
                        buttonType = runCatching {
                            BotsiButtonType.valueOf(get("button_type").toCapitalizedString())
                        }.getOrDefault(BotsiButtonType.Icon),
                        buttonAlign = runCatching {
                            BotsiAlign.valueOf(get("button_align").toCapitalizedString())
                        }.getOrDefault(BotsiAlign.Left),
                        delay = runCatching { get("delay").asLong }.getOrNull(),
                        style = runCatching { buttonStyleMapper.map(get("style")) }.getOrNull(),
                        text = runCatching { textMapper.map(get("text")) }.getOrNull(),
                        icon = runCatching {
                            with(get("icon").asJsonObject) {
                                BotsiIconStyle(
                                    type = runCatching {
                                        BotsiButtonIconType.valueOf(get("type").toCapitalizedString())
                                    }.getOrDefault(BotsiButtonIconType.None),
                                    color = runCatching { (get("color") ?: get("fill_color") ?: get("fillColor")).toHexColorIfPossible() }.getOrNull(),
                                    opacity = runCatching { get("opacity").asFloat }.getOrNull(),
                                )
                            }
                        }.getOrNull(),
                    )
                }
            }
        }
    }

}
