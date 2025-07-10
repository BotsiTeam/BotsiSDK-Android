package com.botsi.domain.model

import androidx.annotation.Keep

@Keep
data class BotsiUpdateProfileParameters(
    val birthday: String? = null,
    val email: String? = null,
    val userName: String? = null,
    val gender: String? = null,
    val phone: String? = null,
    val custom: List<CustomAttributesEntry> = emptyList()
) {

    @Keep
    data class CustomAttributesEntry(
        val key: String? = null,
        val value: String? = null,
        val id: String? = null,
    )

}
