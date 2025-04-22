package com.botsi.data.model.dto

import androidx.annotation.RestrictTo

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal data class BotsiPaywallDto(
    val id: Long? = null,
    val name: String? = null,
    val abTestId: Long? = null,
    val remoteConfigs: String? = null,
    val revision: Long? = null,
    val sourceProducts: List<BotsiProductDto>? = null,
)
