package com.botsi.data.model.request

import androidx.annotation.Keep
import androidx.annotation.RestrictTo
import com.botsi.data.model.dto.BotsiInstallationMetaDto
import com.google.gson.annotations.SerializedName

@Keep
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal class BotsiCreateProfileRequest(
    @SerializedName("meta")
    private val meta: BotsiInstallationMetaDto?
)
