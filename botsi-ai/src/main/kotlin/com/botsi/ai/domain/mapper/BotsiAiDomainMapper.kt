package com.botsi.ai.domain.mapper

import com.android.billingclient.api.ProductDetails
import com.botsi.ai.data.model.BotsiAiPaywallDto
import com.botsi.ai.data.model.BotsiAiProductDto
import com.botsi.ai.data.model.BotsiAiPurchasableProductDto
import com.botsi.ai.domain.model.BotsiAiPaywall
import com.botsi.ai.domain.model.BotsiAiProduct
import com.botsi.ai.domain.model.BotsiAiSubscriptionOfferDetails

fun BotsiAiPaywallDto.toDomain(marketProducts: List<ProductDetails>): BotsiAiPaywall {
    return BotsiAiPaywall(
        id = id ?: 0,
        aiPricingModelId = aiPricingModelId ?: 0,
        externalId = externalId.orEmpty(),
        name = name.orEmpty(),
        remoteConfigs = remoteConfigs.orEmpty(),
        revision = revision ?: 0L,
        isExperiment = isExperiment ?: false,
        sourceProducts = sourceProducts.orEmpty().map {
            it.toDomain(
                marketProducts.first { marketProduct ->
                    marketProduct.productId == it.sourceProductId
                }
            )
        }
    )
}

fun BotsiAiPaywall.toDto(): BotsiAiPaywallDto {
    return BotsiAiPaywallDto(
        id = id,
        aiPricingModelId = aiPricingModelId,
        externalId = externalId,
        name = name,
        remoteConfigs = remoteConfigs,
        revision = revision,
        isExperiment = isExperiment,
        sourceProducts = sourceProducts.map { it.toDto() }
    )
}

fun BotsiAiProductDto.toDomain(productDetails: ProductDetails): BotsiAiProduct {
    return BotsiAiProduct(
        botsiProductId = botsiProductId ?: 0,
        sourceProductId = sourceProductId.orEmpty(),
        isConsumable = isConsumable ?: false,
        basePlanId = basePlanId.orEmpty(),
        offerIds = offerIds.orEmpty(),
        productDetails = productDetails
    )
}

fun BotsiAiProduct.toDto(): BotsiAiProductDto {
    return BotsiAiProductDto(
        botsiProductId = botsiProductId,
        sourceProductId = sourceProductId,
        isConsumable = isConsumable,
        basePlanId = basePlanId,
        offerIds = offerIds
    )
}

fun BotsiAiProductDto.toPurchasableProductDto(productDetails: ProductDetails): BotsiAiPurchasableProductDto {
    return BotsiAiPurchasableProductDto(
        isConsumable = isConsumable ?: false,
        currentSubOfferDetails = BotsiAiSubscriptionOfferDetails.from(
            productDetails.subscriptionOfferDetails?.firstOrNull()
        ),
        productDetails = productDetails,
    )
}
