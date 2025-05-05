package com.botsi.data.repository

import androidx.annotation.RestrictTo
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.botsi.data.model.dto.BotsiPaywallDto
import com.botsi.data.model.dto.BotsiProfileDto
import com.botsi.data.model.dto.BotsiPurchasableProductDto
import com.botsi.data.model.dto.BotsiPurchaseRecordDto
import com.botsi.data.model.dto.BotsiUpdateProfileParametersDto
import kotlinx.coroutines.flow.Flow

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal interface BotsiRepository {

    val profileStateFlow: Flow<BotsiProfileDto>

    fun getOrCreateProfile(customerUserId: String?): Flow<BotsiProfileDto>

    fun updateProfile(customerUserId: String?,params: BotsiUpdateProfileParametersDto?): Flow<BotsiProfileDto>

    fun validatePurchase(
        purchase: Purchase,
        productDetails: BotsiPurchasableProductDto,
    ): Flow<BotsiProfileDto>

    fun getProductIds(): Flow<List<String>>

    fun getPaywall(placementId: String): Flow<BotsiPaywallDto>
    fun syncPurchases(details: List<Pair<BotsiPurchaseRecordDto, ProductDetails>>): Flow<BotsiProfileDto>
    fun clearCache()

}