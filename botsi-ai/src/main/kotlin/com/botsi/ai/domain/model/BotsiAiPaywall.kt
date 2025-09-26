package com.botsi.ai.domain.model

import androidx.annotation.Keep

@Keep
data class BotsiAiPaywall(
    val id: Long,
    val aiPricingModelId: Long,
    val externalId: String,
    val name: String,
    val remoteConfigs: String,
    val revision: Long,
    val isExperiment: Boolean,
    val sourceProducts: List<BotsiAiProduct>,
)