package com.competo.botsi.view.mapper

import androidx.annotation.RestrictTo
import com.competo.botsi.view.model.content.BotsiLayoutContentModel
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal class BotsiLayoutContentMapper(

) {

    suspend fun map(json: JsonElement): BotsiLayoutContentModel {
        return withContext(Dispatchers.Default) {
            BotsiLayoutContentModel(
                id = 0
            )
        }
    }
}