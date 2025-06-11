package com.botsi.data.repository

import androidx.annotation.RestrictTo
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.botsi.data.http.BotsiHttpManager
import com.botsi.data.model.dto.BotsiPaywallDto
import com.botsi.data.model.dto.BotsiProfileDto
import com.botsi.data.model.dto.BotsiPurchasableProductDto
import com.botsi.data.model.dto.BotsiPurchaseRecordDto
import com.botsi.data.model.dto.BotsiUpdateProfileParametersDto
import com.botsi.data.service.BotsiInstallationMetaRetrieverService
import com.botsi.data.storage.BotsiStorageManager
import com.google.gson.JsonElement
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class BotsiRepositoryImpl(
    private val httpManager: BotsiHttpManager,
    private val storageManager: BotsiStorageManager,
    private val metaRetrieverService: BotsiInstallationMetaRetrieverService,
) : BotsiRepository {

    private val _profileStateFlow = MutableSharedFlow<BotsiProfileDto>(replay = 1)
    override val profileStateFlow: Flow<BotsiProfileDto> = _profileStateFlow

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getOrCreateProfile(customerUserId: String?): Flow<BotsiProfileDto> {
        return when {
            storageManager.isProfileTemp ->
                metaRetrieverService.installationMetaFlow(storageManager.deviceId)
                    .flatMapLatest { installationMeta ->
                        flow {
                            emit(
                                httpManager.createProfile(
                                    storageManager.profileId,
                                    customerUserId,
                                    installationMeta
                                )
                            )
                        }
                    }
                    .onEach { storageManager.profile = it }

            storageManager.isProfileUnSynced -> flow { emit(httpManager.getProfile(storageManager.profileId)) }
                .onEach { storageManager.profile = it }

            else -> flow { emit(storageManager.profile!!) }
        }
            .onEach { _profileStateFlow.tryEmit(it) }
    }

    override fun updateProfile(
        customerUserId: String?,
        params: BotsiUpdateProfileParametersDto?
    ): Flow<BotsiProfileDto> {
        return flow { emit(httpManager.updateProfile(storageManager.profileId, customerUserId, params)) }
            .onEach {
                storageManager.profile = it
                _profileStateFlow.tryEmit(it)
            }
    }

    override fun validatePurchase(
        purchase: Purchase,
        product: BotsiPurchasableProductDto
    ): Flow<BotsiProfileDto> {
        return flow {
            emit(
                httpManager.validatePurchase(
                    storageManager.profileId,
                    purchase,
                    product
                )
            )
        }
            .onEach {
                storageManager.profile = it
                _profileStateFlow.tryEmit(it)
            }
    }

    override fun getProductIds(): Flow<List<String>> {
        return flow { emit(httpManager.getProductIds()) }
    }

    override fun getPaywall(placementId: String): Flow<BotsiPaywallDto> {
        return flow { emit(httpManager.getPaywall(placementId, storageManager.profileId)) }
    }

    override fun getPaywallViewConfiguration(paywallId: Long): Flow<JsonElement> {
        return flow { emit(httpManager.getPaywallViewConfiguration(paywallId)) }
    }

    override fun syncPurchases(details: List<Pair<BotsiPurchaseRecordDto, ProductDetails>>): Flow<BotsiProfileDto> {
        return flow {
            emit(httpManager.syncPurchases(storageManager.profileId, details))
        }
            .onEach {
                storageManager.profile = it
                _profileStateFlow.tryEmit(it)
            }
    }

    override fun clearCache() {
        storageManager.clearCache()
    }
}