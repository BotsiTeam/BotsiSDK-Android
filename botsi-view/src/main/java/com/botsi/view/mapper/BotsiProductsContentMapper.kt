package com.botsi.view.mapper

import com.botsi.view.model.content.BotsiProductsContent
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


internal class BotsiProductsContentMapper {
    suspend fun map(json: JsonElement): BotsiProductsContent {
        return withContext(Dispatchers.Default) {
            BotsiProductsContent()
        }
    }
}