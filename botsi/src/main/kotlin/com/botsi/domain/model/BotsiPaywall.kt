package com.botsi.domain.model

data class BotsiPaywall(
    val id: Long,
    val placementId: String,
    val name: String,
    val abTestId: Long,
    val remoteConfigs: String,
    val revision: Long,
    val sourceProducts: List<BotsiProduct>,
)
