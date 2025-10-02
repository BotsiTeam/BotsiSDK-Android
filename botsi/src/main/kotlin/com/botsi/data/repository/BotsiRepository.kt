package com.botsi.data.repository

import androidx.annotation.RestrictTo
import com.android.billingclient.api.ProductDetails
import com.botsi.data.model.dto.BotsiPaywallDto
import com.botsi.data.model.dto.BotsiProfileDto
import com.botsi.data.model.dto.BotsiPurchasableProductDto
import com.botsi.data.model.dto.BotsiPurchaseRecordDto
import com.botsi.data.model.dto.BotsiSyncPurchaseDto
import com.botsi.data.model.dto.BotsiUpdateProfileParametersDto
import com.botsi.domain.model.BotsiPurchase
import com.google.gson.JsonElement
import kotlinx.coroutines.flow.Flow

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal interface BotsiRepository {

    val profileStateFlow: Flow<BotsiProfileDto>

    fun getOrCreateProfile(customerUserId: String?): Flow<BotsiProfileDto>

    fun updateProfile(customerUserId: String?,params: BotsiUpdateProfileParametersDto?): Flow<BotsiProfileDto>

    fun validatePurchase(
        purchase: BotsiPurchase,
        productDetails: BotsiPurchasableProductDto,
    ): Flow<BotsiProfileDto>

    fun getProductIds(): Flow<List<String>>

    fun getPaywall(placementId: String): Flow<BotsiPaywallDto>

    fun getPaywallViewConfiguration(paywallId: Long): Flow<JsonElement>

    fun syncPurchases(details: List<Pair<BotsiPurchaseRecordDto, ProductDetails>>): Flow<BotsiProfileDto>

    fun saveSyncedPurchases(purchases: List<BotsiSyncPurchaseDto>)
    fun getSyncedPurchases(): List<BotsiSyncPurchaseDto>

    fun clearCache()

}