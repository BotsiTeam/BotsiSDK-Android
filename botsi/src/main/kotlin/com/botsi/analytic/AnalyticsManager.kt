package com.botsi.analytic

import androidx.annotation.RestrictTo
import com.botsi.BotsiException
import com.botsi.data.factory.BotsiRequestFactory
import com.botsi.data.http.client.BotsiHttpClient
import com.botsi.data.model.response.BotsiResponse
import com.botsi.data.service.BotsiInstallationMetaRetrieverService
import com.botsi.data.storage.BotsiStorageManager
import com.botsi.scope.launch
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retryWhen
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal class AnalyticsManager(
    private val storageManager: BotsiStorageManager,
    private val httpClient: BotsiHttpClient,
    private val requestFactory: BotsiRequestFactory,
    private val installationMetaRetrieverService: BotsiInstallationMetaRetrieverService,
) : AnalyticsTracker {

    private val eventFlow = MutableSharedFlow<AnalyticsEvent>(extraBufferCapacity = 1)

    init {
        startProcessingEvents()
    }

    @OptIn(ExperimentalCoroutinesApi::class, ExperimentalTime::class)
    private fun startProcessingEvents() {
        launch {
            eventFlow
                .flatMapConcat { event ->
                    sendData(listOf(event))
                        .retryIfNecessary(DEFAULT_RETRY_COUNT)
                }
                .collect()
        }
    }

    override fun trackEvent(event: AnalyticsEvent) {
        launch {
            val meta =
                installationMetaRetrieverService.installationMetaFlow(storageManager.deviceId)
                    .first()
            eventFlow.tryEmit(
                event.copy(
                    profileId = storageManager.profileId,
                    timestamp = System.currentTimeMillis(),
                    store = "play-store",
                    country = meta.storeCountry,
                )
            )
        }
    }

    private fun sendData(filteredEvents: List<AnalyticsEvent>) = flow {
        if (filteredEvents.isEmpty()) {
            emit(Unit)
            return@flow
        }

        val response = httpClient.newRequest<Unit>(
            requestFactory.sendAnalyticsEventsRequest(filteredEvents),
            Unit::class.java
        )
        when (response) {
            is BotsiResponse.Success -> {
                emit(Unit)
            }

            is BotsiResponse.Error -> {
                throw response.error
            }
        }
    }

    private fun <T> Flow<T>.bufferList(timeInMillis: Long): Flow<List<T>> {
        return flow {
            val buffer = mutableListOf<T>()
            (1..timeInMillis)
                .asFlow()
                .onEach { delay(1000L) }
                .collect {
                    if (it == timeInMillis) {
                        emit(buffer)
                        buffer.clear()
                    }
                }
            this@bufferList.collect {
                buffer.add(it)
            }
        }
    }

    private fun <T> Flow<T>.retryIfNecessary(maxAttemptCount: Long = DEFAULT_RETRY_COUNT): Flow<T> =
        this.retryWhen { error, attempt ->
            error is BotsiException && (maxAttemptCount in 0..attempt)
        }

    companion object {
        private const val DEFAULT_RETRY_COUNT = 3L
    }

}