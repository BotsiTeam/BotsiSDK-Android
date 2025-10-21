package com.botsi.ai.data.model

import androidx.annotation.Keep
import androidx.annotation.RestrictTo

@Keep
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class BotsiAiProductDto(
    val botsiProductId: Int? = null,
    val sourceProductId: String? = null,
    val isConsumable: Boolean? = null,
    val basePlanId: String? = null,
    val offerIds: List<String>? = null,
)
