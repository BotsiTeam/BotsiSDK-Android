package com.botsi.domain.model

import androidx.annotation.Keep

@Keep
enum class BotsiReplacementMode {
    WITH_TIME_PRORATION,
    CHARGE_PRORATED_PRICE,
    WITHOUT_PRORATION,
    DEFERRED,
    CHARGE_FULL_PRICE,
}
