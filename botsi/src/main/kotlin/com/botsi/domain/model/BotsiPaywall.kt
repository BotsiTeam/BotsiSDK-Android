package com.botsi.domain.model

import androidx.annotation.Keep

@Keep
data class BotsiPaywall(
    val id: Long,
    val placementId: String,
    val name: String,
    val abTestId: Long,
    val remoteConfigs: String,
    val revision: Long,
    val sourceProducts: List<BotsiBackendProduct>,
)
