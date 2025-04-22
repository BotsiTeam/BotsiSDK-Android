package com.botsi.domain.model

data class BotsiUpdateProfileParameters(
    val birthday: String,
    val email: String,
    val userName: String,
    val gender: String,
    val phone: String,
    val custom: List<CustomAttributesEntry>
) {

    data class CustomAttributesEntry(
        val key: String,
        val value: String,
        val id: String,
    )

}