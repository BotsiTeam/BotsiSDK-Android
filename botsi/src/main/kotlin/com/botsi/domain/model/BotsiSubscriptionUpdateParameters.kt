package com.botsi.domain.model

data class BotsiSubscriptionUpdateParameters(
    val oldSubVendorProductId: String,
    val replacementMode: BotsiReplacementMode,
)