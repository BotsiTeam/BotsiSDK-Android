package com.botsi.view.mapper

import androidx.annotation.RestrictTo
import com.botsi.view.model.content.BotsiListNestedContent
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal class BotsiListNestedContentMapper(

) {

    suspend fun map(json: JsonElement): BotsiListNestedContent {
        return withContext(Dispatchers.Default) {
            BotsiListNestedContent()
        }
    }
}