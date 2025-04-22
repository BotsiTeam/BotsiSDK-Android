package com.botsi.data.model.response

import androidx.annotation.RestrictTo
import com.botsi.BotsiException

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal sealed class BotsiResponse<out R> {
    data class Success<out T>(val body: T) : BotsiResponse<T>()
    data class Error(val error: BotsiException) : BotsiResponse<Nothing>()
}