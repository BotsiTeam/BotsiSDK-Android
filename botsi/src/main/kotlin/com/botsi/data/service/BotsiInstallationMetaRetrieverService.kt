package com.botsi.data.service

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import androidx.annotation.RestrictTo
import com.botsi.BuildConfig
import com.botsi.data.model.dto.BotsiInstallationMetaDto
import com.botsi.data.service.retriever.BotsiAdIdRetriever
import com.botsi.data.service.retriever.BotsiAppSetIdRetriever
import com.botsi.data.service.retriever.BotsiStoreCountryRetriever
import com.botsi.data.service.retriever.BotsiUserAgentRetriever
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import java.util.Locale
import java.util.TimeZone

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class BotsiInstallationMetaRetrieverService(
    private val appContext: Context,
    private val appSetIdRetriever: BotsiAppSetIdRetriever,
    private val adIdRetriever: BotsiAdIdRetriever,
    private val userAgentRetriever: BotsiUserAgentRetriever,
    private val storeCountryRetriever: BotsiStoreCountryRetriever,
) {
    private var botsiSdkVersion: String = BuildConfig.VERSION_NAME

    private val device: String =
        (if (Build.MODEL.startsWith(Build.MANUFACTURER)) Build.MODEL else "${Build.MANUFACTURER} ${Build.MODEL}")
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ENGLISH) else it.toString() }

    private val locale: String
        get() {
            val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                appContext.resources.configuration.locales.get(0)
            } else {
                appContext.resources.configuration.locale
            }

            return if (locale.country.isNullOrEmpty()) locale.language else "${locale.language}-${locale.country}"
        }

    private var os: String = Build.VERSION.RELEASE

    private var platform: String = "android"

    private var timezone: String = TimeZone.getDefault().id

    @SuppressLint("HardwareIds")
    private var androidId: String = Settings.Secure.getString(appContext.contentResolver, Settings.Secure.ANDROID_ID)

    private val appBuildAndVersion: Pair<String, String> by lazy {
        appContext.packageManager.getPackageInfo(appContext.packageName, 0)
            .let { packageInfo ->
                val appBuild = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    "${packageInfo.longVersionCode}"
                } else {
                    "${packageInfo.versionCode}"
                }
                val appVersion = packageInfo.versionName.orEmpty()

                appBuild to appVersion
            }
    }

    fun installationMetaFlow(deviceId: String): Flow<BotsiInstallationMetaDto> =
        combineTransform(
            adIdRetriever.getAdIdIfAvailable(appContext),
            appSetIdRetriever.getAppSetIdIfAvailable(appContext),
            userAgentRetriever.getUserAgentIfAvailable(appContext),
            storeCountryRetriever.getCountryIfAvailable(),
        ) { advertisingId, appSetId, userAgent, storeCountry ->
            emit(
                BotsiInstallationMetaDto(
                    deviceId = deviceId,
                    botsiSdkVersion = botsiSdkVersion,
                    appBuild = appBuildAndVersion.first,
                    appVersion = appBuildAndVersion.second,
                    device = device,
                    locale = locale,
                    os = os,
                    platform = platform,
                    timezone = timezone,
                    userAgent = userAgent,
                    advertisingId = advertisingId,
                    appSetId = appSetId,
                    androidId = androidId,
                    storeCountry = storeCountry,
                )
            )
        }

}