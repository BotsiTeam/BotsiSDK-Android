package com.botsi.domain.interactor.profile

import androidx.annotation.RestrictTo
import com.botsi.data.repository.BotsiRepository
import com.botsi.domain.mapper.toDomain
import com.botsi.domain.mapper.toDto
import com.botsi.domain.model.BotsiProfile
import com.botsi.domain.model.BotsiUpdateProfileParameters
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.timeout
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class BotsiProfileInteractorImpl(
    private val repository: BotsiRepository,
) : BotsiProfileInteractor {

    override val customerUserId: String?
        get() = repository.customerUserId

    override val profileFlow: Flow<BotsiProfile>
        get() = repository.profileStateFlow.map { it.toDomain() }

    override fun getOrCreateProfile(): Flow<BotsiProfile> {
        return repository.getOrCreateProfile()
            .map { it.toDomain() }
    }

    override fun clearCache() {
        repository.clearCache()
    }

    override fun updateProfile(customerUserId: String?, params: BotsiUpdateProfileParameters?): Flow<BotsiProfile> {
        return repository.updateProfile(customerUserId, params?.toDto())
            .map { it.toDomain() }
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    override fun <T> doOnProfileReady(flow: Flow<T>): Flow<T> {
        return profileFlow
            .take(1)
            .timeout(TIMEOUT_SEC.toDuration(DurationUnit.SECONDS))
            .retryWhen { cause, attempt -> cause is TimeoutCancellationException && (RETRY_DEFAULT_COUNT in 0..attempt) }
            .flatMapConcat { flow }
    }

    private companion object {
        const val TIMEOUT_SEC = 10
        const val RETRY_DEFAULT_COUNT = 3
    }

}