package com.botsi.ai.data.model

import androidx.annotation.Keep
import androidx.annotation.RestrictTo
import com.google.gson.annotations.SerializedName

@Keep
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
data class BotsiAiInstallationMetaDto(
    @SerializedName("deviceId")
    val deviceId: String,
    @SerializedName("appBuild")
    val appBuild: String,
    @SerializedName("appVersion")
    val appVersion: String,
    @SerializedName("device")
    val device: String,
    @SerializedName("locale")
    val locale: String?,
    @SerializedName("os")
    val os: String,
    @SerializedName("platform")
    val platform: String,
    @SerializedName("timezone")
    val timezone: String,
    @SerializedName("userAgent")
    val userAgent: String?,
    @SerializedName("advertisingId")
    val advertisingId: String,
    @SerializedName("androidAppSetId")
    val appSetId: String,
    @SerializedName("androidId")
    val androidId: String,
    @SerializedName("country")
    val country: String?,
    @SerializedName("customerUserId")
    val customerUserId: String?,
    @SerializedName("ip")
    val ip: String?,
)
