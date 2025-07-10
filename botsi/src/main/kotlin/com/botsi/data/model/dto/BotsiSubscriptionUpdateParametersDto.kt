package com.botsi.data.model.dto

import androidx.annotation.Keep
import com.botsi.domain.model.BotsiReplacementMode

@Keep
class BotsiSubscriptionUpdateParametersDto(
    oldSubVendorProductId: String,
    val replacementMode: BotsiReplacementMode,
) {
    val oldSubVendorProductId: String = oldSubVendorProductId.split(":")[0]
}
