package com.botsi.data.service.retriever

import androidx.annotation.RestrictTo
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Semaphore
import java.net.URL

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal class BotsiNetworkIpRetriever {

    private val semaphore = Semaphore(1)

    @Volatile
    private var cachedIp: String? = null

    fun getIpIfAvailable(): Flow<String> =
        flow {
            cachedIp?.let { cachedAdId ->
                emit(cachedAdId)
                return@flow
            }

            semaphore.acquire()
            cachedIp?.let { cachedIp ->
                semaphore.release()
                emit(cachedIp)
                return@flow
            }

            val ip = try {
                getMyPublicIp()
            } catch (e: Exception) {
                null
            }

            cachedIp = ip
            semaphore.release()
            emit(ip.orEmpty())
        }

    private suspend fun getMyPublicIp(): String {
        return coroutineScope {
            val url = URL("https://api.ipify.org")
            val httpsURLConnection = url.openConnection()
            val iStream = httpsURLConnection.getInputStream()
            val buff = ByteArray(1024)
            val read = iStream.read(buff)
            String(buff, 0, read)
        }
    }
}