package com.botsi.data.http.client

import androidx.annotation.RestrictTo
import com.botsi.data.model.request.BotsiRequest
import com.botsi.data.model.response.BotsiResponse
import java.lang.reflect.Type

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal interface BotsiHttpClient {
    fun <T> newRequest(request: BotsiRequest, typeOfT: Type): BotsiResponse<T>
}