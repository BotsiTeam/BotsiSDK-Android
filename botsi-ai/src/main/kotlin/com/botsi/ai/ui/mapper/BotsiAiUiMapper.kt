package com.botsi.ai.ui.mapper

import com.botsi.ai.domain.model.BotsiAiPaywall
import com.botsi.ai.domain.model.BotsiAiProduct
import com.botsi.ai.ui.model.BotsiAiPaywallUi
import com.botsi.ai.ui.model.BotsiAiProductUi
import com.botsi.ai.ui.model.BotsiUiPaywallType

fun BotsiAiPaywall.toUi(): BotsiAiPaywallUi =
    BotsiAiPaywallUi(
        id = id,
        aiPricingModelId = aiPricingModelId,
        externalId = externalId,
        name = name,
        remoteConfigs = remoteConfigs,
        revision = revision,
        isExperiment = isExperiment,
        sourceProducts = sourceProducts.map { it.toUi() }
    )

fun BotsiAiProduct.toUi(): BotsiAiProductUi =
    BotsiAiProductUi(
        botsiProductId = botsiProductId,
        sourceProductId = sourceProductId,
        isConsumable = isConsumable,
        basePlanId = basePlanId,
        offerIds = offerIds,
        productDetails = productDetails
    )

fun BotsiAiPaywallUi.toDomain(): BotsiAiPaywall =
    BotsiAiPaywall(
        id = id,
        aiPricingModelId = aiPricingModelId,
        externalId = externalId,
        name = name,
        remoteConfigs = remoteConfigs,
        revision = revision,
        isExperiment = isExperiment,
        sourceProducts = sourceProducts.map { it.toDomain() }
    )


fun BotsiAiProductUi.toDomain(): BotsiAiProduct =
    BotsiAiProduct(
        botsiProductId = botsiProductId,
        sourceProductId = sourceProductId,
        isConsumable = isConsumable,
        basePlanId = basePlanId,
        offerIds = offerIds,
        productDetails = productDetails!!
    )

fun String.toPaywallType(): BotsiUiPaywallType =
    BotsiUiPaywallType.entries.firstOrNull { it.id == this }
        ?: BotsiUiPaywallType.None
