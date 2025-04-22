package com.botsi.data.model.dto

import com.botsi.domain.model.BotsiReplacementMode

class BotsiSubscriptionUpdateParametersDto(
    oldSubVendorProductId: String,
    val replacementMode: BotsiReplacementMode,
) {
    val oldSubVendorProductId: String = oldSubVendorProductId.split(":")[0]
}