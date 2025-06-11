package com.botsi.domain.interactor.products

import androidx.annotation.RestrictTo
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.botsi.domain.model.BotsiPaywall
import com.botsi.domain.model.BotsiProfile
import com.google.gson.JsonElement
import kotlinx.coroutines.flow.Flow

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal interface BotsiProductsInteractor {
    fun getProductsIds(): Flow<List<ProductDetails>>
    fun getPaywall(placementId: String): Flow<BotsiPaywall>
    fun getPaywallViewConfiguration(paywallId: Long): Flow<JsonElement>
}