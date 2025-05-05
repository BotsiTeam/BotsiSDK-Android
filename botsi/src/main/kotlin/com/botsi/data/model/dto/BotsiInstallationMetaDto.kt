package com.botsi.data.model.dto

import androidx.annotation.RestrictTo
import com.google.gson.annotations.SerializedName

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal data class BotsiInstallationMetaDto(
    @SerializedName("deviceId")
    val deviceId: String,
    @SerializedName("botsiSdkVersion")
    val botsiSdkVersion: String,
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
    @SerializedName("storeCountry")
    val storeCountry: String?,
    @SerializedName("customerUserId")
    val customerUserId: String?,
)