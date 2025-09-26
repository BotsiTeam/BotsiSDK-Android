package com.botsi.ai.data.model

import com.google.gson.annotations.SerializedName

class BotsiAiGetPaywallRequestDto(
    @SerializedName("deviceTypeModel") val deviceTypeModel: String,
    @SerializedName("osVersion") val osVersion: String,
    @SerializedName("languageLocale") val languageLocale: String,
    @SerializedName("placementId") val placementId: String,
    @SerializedName("profileId") val profileId: String,
    @SerializedName("store") val store: String,
    @SerializedName("attributionSource") val attributionSource: String? = null,
)