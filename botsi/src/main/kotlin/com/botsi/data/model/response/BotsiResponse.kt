package com.botsi.data.model.response

import androidx.annotation.Keep
import androidx.annotation.RestrictTo
import com.botsi.BotsiException

@Keep
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal sealed class BotsiResponse<out R> {
    @Keep
    data class Success<out T>(val body: T) : BotsiResponse<T>()
    @Keep
    data class Error(val error: BotsiException) : BotsiResponse<Nothing>()
}
