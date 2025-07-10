package com.botsi.domain.model

import androidx.annotation.Keep

@Keep
data class BotsiSubscriptionUpdateParameters(
    val oldSubVendorProductId: String,
    val replacementMode: BotsiReplacementMode = BotsiReplacementMode.WITHOUT_PRORATION,
)
