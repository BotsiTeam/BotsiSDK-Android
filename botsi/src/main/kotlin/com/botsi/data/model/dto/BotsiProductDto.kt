package com.botsi.data.model.dto

import androidx.annotation.Keep
import androidx.annotation.RestrictTo

@Keep
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal class BotsiProductDto(
    val botsiProductId: Int? = null,
    val sourceProductId: String? = null,
    val isConsumable: Boolean? = null,
    val basePlanId: String? = null,
    val offerIds: List<String>? = null,
)
