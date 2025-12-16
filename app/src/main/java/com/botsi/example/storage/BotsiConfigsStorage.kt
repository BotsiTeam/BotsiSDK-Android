package com.botsi.example.storage

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

    var secretKey: String
        set(value) {
            preferences.edit {
                putString(SECRET_KEY, value)
            }
        }
        get() = preferences.getString(SECRET_KEY, "")
            .orEmpty()
            .ifEmpty { "sk_ElLDOD5E8Eq4v5y.XlJOTtEIqK9bL15J3C7ofuxag" }

    var placementId: String
        set(value) {
            preferences.edit {
                putString(PLACEMENT_ID, value)
            }
        }
        get() = preferences.getString(PLACEMENT_ID, "")
            .orEmpty()
            .ifEmpty { "botsi-test-app-placement" }

    companion object {
        private const val APP_KEY = "APP_KEY"
        private const val SECRET_KEY = "SECRET_KEY"
        private const val PLACEMENT_ID = "PLACEMENT_ID"
    }

}