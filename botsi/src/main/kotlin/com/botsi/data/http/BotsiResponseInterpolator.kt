package com.botsi.data.http

import androidx.annotation.RestrictTo
import com.botsi.BotsiException
import com.botsi.data.model.response.BotsiResponse
import com.google.gson.Gson
import java.io.InputStream
import java.lang.reflect.Type
import java.net.HttpURLConnection
import java.util.logging.Level.INFO
import java.util.logging.Logger
import java.util.zip.GZIPInputStream

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class BotsiResponseInterpolator(
    private val gson: Gson,
) {

    fun <T> process(connection: HttpURLConnection, typeOfT: Type): BotsiResponse<T> {
        val isInGzip =
            connection.getHeaderField("Content-Encoding")?.contains("gzip", true) ?: false

        if (connection.isSuccessful()) {
            val responseStr = toStringUtf8(connection.inputStream, isInGzip)
            log("Request is successful. ${connection.url} Response: $responseStr")
            return BotsiResponse.Success(gson.fromJson<T>(responseStr, typeOfT))
        } else {
            val responseStr = toStringUtf8(connection.errorStream, isInGzip)
            val errorMessage =
                "Request is unsuccessful. ${connection.url} Code: ${connection.responseCode}, Response: $responseStr"
            log(errorMessage)
            val e = BotsiException(
                cause = IllegalStateException(errorMessage),
                message = errorMessage,
                code = connection.responseCode,
            )
            return BotsiResponse.Error(e)
        }
    }

    private fun log(message: String) {
        Logger.getAnonymousLogger().log(INFO, message)
    }

    private fun toStringUtf8(inputStream: InputStream, isInGzip: Boolean): String {
        val reader = if (isInGzip) GZIPInputStream(inputStream) else inputStream
        return reader.bufferedReader(Charsets.UTF_8).use { it.readText() }
    }

    private fun HttpURLConnection.isSuccessful() = responseCode in 200..299

}