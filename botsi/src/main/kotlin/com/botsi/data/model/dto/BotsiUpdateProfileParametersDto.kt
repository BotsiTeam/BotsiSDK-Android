package com.botsi.data.model.dto

import androidx.annotation.Keep
import com.botsi.domain.model.BotsiProfile

@Keep
internal data class BotsiUpdateProfileParametersDto(
    val birthday: String?,
    val email: String?,
    val userName: String?,
    val gender: String?,
    val phone: String?,
    val custom: List<BotsiProfile.CustomEntry>
)
