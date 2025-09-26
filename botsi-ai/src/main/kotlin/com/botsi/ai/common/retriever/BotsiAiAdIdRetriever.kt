package com.botsi.ai.common.retriever

import android.content.Context
import androidx.annotation.RestrictTo
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Semaphore

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class BotsiAiAdIdRetriever {

    private val adIdSemaphore = Semaphore(1)

    @Volatile
    private var cachedAdvertisingId: String? = null

    fun getAdIdIfAvailable(context: Context): Flow<String> =
        flow {
            cachedAdvertisingId?.let { cachedAdId ->
                emit(cachedAdId)
                return@flow
            }

            adIdSemaphore.acquire()
            cachedAdvertisingId?.let { cachedAdId ->
                adIdSemaphore.release()
                emit(cachedAdId)
                return@flow
            }

            val adId = try {
                AdvertisingIdClient.getAdvertisingIdInfo(context)
                    .takeIf { !it.isLimitAdTrackingEnabled }?.id
            } catch (e: Exception) {
                null
            }

            cachedAdvertisingId = adId
            adIdSemaphore.release()
            emit(adId.orEmpty())
        }
}