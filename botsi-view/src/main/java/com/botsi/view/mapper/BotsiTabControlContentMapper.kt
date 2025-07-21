package com.botsi.view.mapper

import com.botsi.view.model.content.BotsiTabControlContent
import com.botsi.view.model.content.BotsiTabControlState
import com.botsi.view.model.content.BotsiTabState
import com.botsi.view.model.content.BotsiButtonStyle
import com.botsi.view.utils.toCapitalizedString
import com.botsi.view.utils.toHexColorIfPossible
import com.botsi.view.utils.toIntList
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class BotsiTabControlContentMapper(
    private val fontMapper: BotsiFontMapper,
    private val buttonStyleMapper: BotsiButtonStyleMapper
) {
    suspend fun map(json: JsonElement): BotsiTabControlContent {
        return withContext(Dispatchers.Default) {
            with(json.asJsonObject) {
                BotsiTabControlContent(
                    selectedTab = runCatching { get("selected_tab").asString }.getOrNull(),
                    tabState = runCatching { 
                        BotsiTabState.valueOf(get("tab_state").toCapitalizedString()) 
                    }.getOrNull(),
                    containerStyle = runCatching { 
                        buttonStyleMapper.map(get("container_style")) 
                    }.getOrNull(),
                    activeState = runCatching { 
                        mapTabControlState(get("active_state")) 
                    }.getOrNull(),
                    inactiveState = runCatching { 
                        mapTabControlState(get("inactive_state")) 
                    }.getOrNull(),
                    tabTextFont = runCatching { 
                        fontMapper.map(get("tab_text_font")) 
                    }.getOrNull(),
                    tabTextSize = runCatching { get("tab_text_size").asInt }.getOrNull(),
                    padding = runCatching { get("padding").toIntList() }.getOrNull(),
                    verticalOffset = runCatching { get("vertical_offset").asInt }.getOrNull()
                )
            }
        }
    }

    private suspend fun mapTabControlState(jsonElement: JsonElement): BotsiTabControlState {
        return with(jsonElement.asJsonObject) {
            BotsiTabControlState(
                stateStyle = runCatching { 
                    buttonStyleMapper.map(get("state_style")) 
                }.getOrNull(),
                padding = runCatching { get("padding").toIntList() }.getOrNull(),
                fontColor = runCatching { (get("font_color") ?: get("fontColor") ?: get("color")).toHexColorIfPossible() }.getOrNull(),
                fontOpacity = runCatching { get("font_opacity").asFloat }.getOrNull()
            )
        }
    }
}
