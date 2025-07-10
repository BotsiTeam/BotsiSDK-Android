package com.botsi.data.model.request

import androidx.annotation.Keep
import androidx.annotation.RestrictTo
import com.botsi.data.model.dto.BotsiInstallationMetaDto
import com.botsi.domain.model.BotsiProfile
import com.google.gson.annotations.SerializedName

@Keep
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal class BotsiUpdateProfileRequest(
    @SerializedName("birthday") val birthday: String?,
    @SerializedName("customer_user_id") val customerUserId: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("userName") val userName: String?,
    @SerializedName("gender") val gender: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("custom") val custom: List<BotsiProfile.CustomEntry>?
)
