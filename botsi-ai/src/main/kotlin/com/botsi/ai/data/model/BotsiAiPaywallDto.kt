package com.botsi.ai.data.model

import androidx.annotation.Keep
import androidx.annotation.RestrictTo

@Keep
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
data class BotsiAiPaywallDto(
    val id: Long? = null,
    val aiPricingModelId: Long? = null,
    val externalId: String? = null,
    val name: String? = null,
    val remoteConfigs: String? = null,
    val revision: Long? = null,
    val isExperiment: Boolean? = null,
    val sourceProducts: List<BotsiAiProductDto>? = null,
)
