package com.botsi.data.http.client

import androidx.annotation.RestrictTo
import com.botsi.data.model.request.BotsiRequest
import com.botsi.data.model.response.BotsiResponse
import java.lang.reflect.Type

/**
 * Interface for a simple HTTP client used by the Botsi SDK.
 *
 * Implementations of this interface are responsible for executing [BotsiRequest]s
 * and returning [BotsiResponse]s.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal interface BotsiHttpClient {
    /**
     * Executes an HTTP request and parses the response body into the specified type.
     *
     * @param request The request to execute.
     * @param typeOfT The type to parse the response body into.
     * @return A [BotsiResponse] containing either the parsed body or an error.
     */
    fun <T> newRequest(request: BotsiRequest, typeOfT: Type): BotsiResponse<T>
}