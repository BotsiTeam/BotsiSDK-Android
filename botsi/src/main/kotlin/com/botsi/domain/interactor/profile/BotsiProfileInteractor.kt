package com.botsi.domain.interactor.profile

import androidx.annotation.RestrictTo
import com.botsi.domain.model.BotsiProfile
import com.botsi.domain.model.BotsiUpdateProfileParameters
import kotlinx.coroutines.flow.Flow

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal interface BotsiProfileInteractor {
    val customerUserId: String?
    val profileFlow: Flow<BotsiProfile>
    fun getOrCreateProfile(): Flow<BotsiProfile>
    fun updateProfile(customerUserId: String?, params: BotsiUpdateProfileParameters?): Flow<BotsiProfile>
    fun <T> doOnProfileReady(flow: Flow<T>): Flow<T>
}