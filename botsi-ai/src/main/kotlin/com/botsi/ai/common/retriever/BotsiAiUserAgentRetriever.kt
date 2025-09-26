package com.botsi.ai.common.retriever

import android.content.Context
import android.webkit.WebSettings
import androidx.annotation.RestrictTo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Semaphore

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class BotsiAiUserAgentRetriever {

    private val semaphore = Semaphore(1)

    @Volatile
    private var cachedUserAgent: String? = null

    fun getUserAgentIfAvailable(context: Context): Flow<String> =
        flow {
            cachedUserAgent?.let { appSetId ->
                emit(appSetId)
                return@flow
            }

            semaphore.acquire()
            cachedUserAgent?.let { appSetId ->
                semaphore.release()
                emit(appSetId)
                return@flow
            }

            val userAgent = try {
                WebSettings.getDefaultUserAgent(context)
            } catch (e: Exception) {
                null
            }

            cachedUserAgent = userAgent
            semaphore.release()
            emit(userAgent.orEmpty())
        }
}