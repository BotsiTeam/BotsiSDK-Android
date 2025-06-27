package com.botsi.domain.model

data class BotsiUpdateProfileParameters(
    val birthday: String? = null,
    val email: String? = null,
    val userName: String? = null,
    val gender: String? = null,
    val phone: String? = null,
    val custom: List<CustomAttributesEntry> = emptyList()
) {

    data class CustomAttributesEntry(
        val key: String? = null,
        val value: String? = null,
        val id: String? = null,
    )

}
