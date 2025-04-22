package com.botsi.data.service.retriever

import androidx.annotation.RestrictTo
import com.botsi.data.google_store.BotsiGoogleStoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Semaphore

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal class BotsiStoreCountryRetriever(
    private val googleStoreManager: BotsiGoogleStoreManager
) {

    private val semaphore = Semaphore(1)

    @Volatile
    private var cachedCountry: String? = null

    fun getCountryIfAvailable(): Flow<String> =
        flow {
            cachedCountry?.let { country ->
                emit(country)
                return@flow
            }

            semaphore.acquire()
            cachedCountry?.let { country ->
                semaphore.release()
                emit(country)
                return@flow
            }

            emitAll(
                googleStoreManager.getStoreCountry()
                    .map { it.orEmpty() }
                    .catch { emit("") }
                    .onEach { country -> cachedCountry = country; semaphore.release() }
            )
        }
}