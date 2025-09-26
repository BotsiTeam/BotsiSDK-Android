package com.botsi.ai.domain.model

import androidx.annotation.Keep

@Keep
enum class BotsiAiReplacementMode {
    WITH_TIME_PRORATION,
    CHARGE_PRORATED_PRICE,
    WITHOUT_PRORATION,
    DEFERRED,
    CHARGE_FULL_PRICE,
}
