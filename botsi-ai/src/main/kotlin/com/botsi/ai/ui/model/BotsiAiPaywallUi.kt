package com.botsi.ai.ui.model

data class BotsiAiPaywallUi(
    val id: Long = 0,
    val aiPricingModelId: Long = 0,
    val externalId: String = "",
    val name: String = "",
    val remoteConfigs: String = "",
    val revision: Long = 0,
    val isExperiment: Boolean = false,
    val sourceProducts: List<BotsiAiProductUi> = emptyList(),
)
