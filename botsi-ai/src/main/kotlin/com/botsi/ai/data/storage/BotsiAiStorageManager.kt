package com.botsi.ai.data.storage

import androidx.annotation.RestrictTo
import java.util.UUID

@RestrictTo(RestrictTo.Scope.LIBRARY)
class BotsiAiStorageManager(
    private val prefsStorage: BotsiAiPrefsStorage,
) {

    fun clearCache() {
        prefsStorage.clearData()
    }

    var profileId: String?
        get() = prefsStorage.getString(PROFILE_ID_KEY)
        set(value) {
            prefsStorage.saveString(PROFILE_ID_KEY, value.orEmpty())
        }

    val deviceId: String
        get() {
            var id = prefsStorage.getString(DEVICE_ID_KEY)
            if (id == null) id = UUID.randomUUID().toString()
            prefsStorage.saveString(DEVICE_ID_KEY, id)
            return id
        }

    private companion object {
        const val PROFILE_ID_KEY = "PROFILE_ID"
        const val DEVICE_ID_KEY = "DEVICE_ID_KEY"
    }
}