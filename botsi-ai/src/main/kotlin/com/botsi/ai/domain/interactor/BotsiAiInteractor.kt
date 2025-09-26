package com.botsi.ai.domain.interactor

import android.app.Activity
import com.botsi.ai.data.repository.BotsiAiRepository
import com.botsi.ai.domain.mapper.toDomain
import com.botsi.ai.domain.mapper.toDto
import com.botsi.ai.domain.model.BotsiAiPaywall
import com.botsi.ai.domain.model.BotsiAiProduct

interface BotsiAiInteractor {
    suspend fun createProfileIfNecessary()
    suspend fun getPaywall(placementId: String): BotsiAiPaywall
    suspend fun makePurchase(
        placementId: String,
        activity: Activity,
        paywall: BotsiAiPaywall,
        product: BotsiAiProduct
    ): Boolean
}

class BotsiAiInteractorImpl(
    private val repository: BotsiAiRepository
) : BotsiAiInteractor {

    override suspend fun createProfileIfNecessary() {
        repository.createProfileIfNecessary()
    }

    override suspend fun getPaywall(placementId: String): BotsiAiPaywall {
        val paywall = repository.getPaywall(placementId)
        val marketProducts = repository.getMarketProducts(
            ids = paywall.sourceProducts.orEmpty().mapNotNull { it.sourcePoductId }
        )

        return paywall.toDomain(marketProducts)
    }

    override suspend fun makePurchase(
        placementId: String,
        activity: Activity,
        paywall: BotsiAiPaywall,
        product: BotsiAiProduct
    ): Boolean {
        return repository.makePurchase(
            placementId = placementId,
            activity = activity,
            paywall = paywall.toDto(),
            product = product.toDto(),
            productDetails = product.productDetails
        )
    }

}