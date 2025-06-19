package com.botsi.view.mapper

import com.botsi.view.model.content.BotsiBlockMeta
import com.botsi.view.model.content.BotsiContentType
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


internal class BotsiBlockMetaMapper {
    suspend fun map(json: JsonElement): BotsiBlockMeta {
        return withContext(Dispatchers.Default) {
            with(json.asJsonObject) {
                BotsiBlockMeta(
                    blockName = runCatching { get("block_name").asString }.getOrNull(),
                    id = runCatching { get("id").asString }.getOrNull(),
                    type = runCatching {
                        BotsiContentType.findByKey(get("type").asString)
                    }.getOrNull(),
                    icon = runCatching { get("icon").asString }.getOrNull(),
                    parentId = runCatching { get("parentId").asString }.getOrNull(),
                    offerType = runCatching { get("offerType").asString }.getOrNull(),
                )
            }
        }
    }
}