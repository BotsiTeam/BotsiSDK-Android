package com.botsi.view.mapper

import com.botsi.view.model.content.BotsiListContent
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


internal class BotsiListContentMapper {
    suspend fun map(json: JsonElement): BotsiListContent {
        return withContext(Dispatchers.Default) {
            BotsiListContent()
        }
    }
}