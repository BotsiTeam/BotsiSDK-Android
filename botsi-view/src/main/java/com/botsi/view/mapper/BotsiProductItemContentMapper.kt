package com.botsi.view.mapper

import com.botsi.view.model.content.BotsiProductItemContent
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


internal class BotsiProductItemContentMapper {
    suspend fun map(json: JsonElement): BotsiProductItemContent {
        return withContext(Dispatchers.Default) {
            BotsiProductItemContent()
        }
    }
}