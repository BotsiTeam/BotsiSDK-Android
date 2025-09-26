package com.botsi.ai.data.model

import androidx.annotation.Keep
import com.botsi.ai.domain.model.BotsiAiReplacementMode

@Keep
class BotsiAiSubscriptionUpdateParametersDto(
    oldSubVendorProductId: String,
    val isOfferPersonalized: Boolean,
    val obfuscatedAccountId: String?,
    val obfuscatedProfileId: String?,
    val replacementMode: BotsiAiReplacementMode,
) {
    val oldSubVendorProductId: String = oldSubVendorProductId.split(":")[0]
}
