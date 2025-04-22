package com.botsi.data.http.client

import androidx.annotation.RestrictTo
import com.botsi.data.http.BotsiResponseInterpolator
import com.botsi.BotsiException
import com.botsi.data.model.request.BotsiRequest
import com.botsi.data.model.response.BotsiResponse
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.lang.reflect.Type
import java.net.HttpURLConnection
import java.net.URL

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class BotsiHttpClientImpl(
    private val responseInterpolator: BotsiResponseInterpolator,
) : BotsiHttpClient {

    override fun <T> newRequest(request: BotsiRequest, typeOfT: Type): BotsiResponse<T> {
        var connection: HttpURLConnection? = null

        try {
            connection = createUrlConnection(request)
            connection.connect()
            return responseInterpolator.process(connection, typeOfT)

        } catch (e: Exception) {
            val message = "Request Error: ${e.localizedMessage ?: e.message}"
            return BotsiResponse.Error(
                BotsiException(
                    cause = e,
                    message = message,
                )
            )
        } finally {
            connection?.disconnect()
        }
    }

    private fun createUrlConnection(request: BotsiRequest) =
        (URL(request.url).openConnection() as HttpURLConnection).apply {
            connectTimeout = TIMEOUT
            readTimeout = TIMEOUT
            requestMethod = request.method.name
            doInput = true

            request.headers?.forEach { header ->
                setRequestProperty(header.key, header.value)
            }

            if (request.method != BotsiRequest.Method.GET) {
                doOutput = true
                val os = outputStream
                BufferedWriter(OutputStreamWriter(os, "UTF-8")).apply {
                    write(request.body)
                    flush()
                    close()
                }
                os.close()
            }
        }

    private companion object {
        const val TIMEOUT = 10000
    }


}