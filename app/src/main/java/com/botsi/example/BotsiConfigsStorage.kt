package com.botsi.example

import android.content.Context
import androidx.core.content.edit

class BotsiConfigsStorage(
    context: Context,
) {

    private val preferences = context.getSharedPreferences("botsi-example", Context.MODE_PRIVATE)

    var appKey: String
        set(value) {
            preferences.edit {
                putString(APP_KEY, value)
            }
        }
        get() = preferences.getString(APP_KEY, "")
            .orEmpty()
            .ifEmpty { "pk_knzfDupGEwEveheF.EEURFTlSDGB47hVva5zRV7Zmc" }

    var placementId: String
        set(value) {
            preferences.edit {
                putString(PLACEMENT_ID, value)
            }
        }
        get() = preferences.getString(PLACEMENT_ID, "")
            .orEmpty()
            .ifEmpty { "botsi-test-app-placement" }

    var paywallId: Long
        set(value) {
            preferences.edit {
                putLong(PAYWALL_ID, value)
            }
        }
        get() = preferences.getLong(PAYWALL_ID, 1L)

    companion object {
        private const val APP_KEY = "APP_KEY"
        private const val PLACEMENT_ID = "PLACEMENT_ID"
        private const val PAYWALL_ID = "PAYWALL_ID"
    }

}