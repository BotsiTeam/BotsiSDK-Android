package com.botsi.ai.data.model

import androidx.annotation.Keep

@Keep
class BotsiAiEventRequest(
    val profileId: String,
    val eventType: String,
    val paywallId: Long,
    val placementId: String,
    val isExperiment: Boolean,
    val aiPricingModelId: Long,
)