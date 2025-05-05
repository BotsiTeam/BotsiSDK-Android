package com.botsi.data.storage

import androidx.annotation.RestrictTo
import com.botsi.data.model.dto.BotsiProfileDto
import java.util.UUID

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class BotsiStorageManager(
    private val prefsStorage: BotsiPrefsStorage,
) {
    fun clearCache() {
        prefsStorage.clearData()
    }

    private var tempProfileId: String? = null

    val profileId: String
        get() {
            val stored = prefsStorage.getString(PROFILE_ID_KEY)
            return when {
                !stored.isNullOrEmpty() -> stored
                !tempProfileId.isNullOrEmpty() -> tempProfileId!!
                else -> {
                    tempProfileId = UUID.randomUUID().toString()
                    return tempProfileId!!
                }
            }
        }

    var profile: BotsiProfileDto?
        get() = prefsStorage.getData(PROFILE_KEY, BotsiProfileDto::class.java)
        set(value) {
            prefsStorage.saveData(PROFILE_KEY, value)
            tempProfileId?.let { prefsStorage.saveString(PROFILE_ID_KEY, it) }
            tempProfileId = null
            syncTime = System.currentTimeMillis()
        }

    val deviceId: String
        get() {
            var id = prefsStorage.getString(DEVICE_ID_KEY)
            if (id == null) id = UUID.randomUUID().toString()
            prefsStorage.saveString(DEVICE_ID_KEY, id)
            return id
        }

    private var syncTime: Long
        get() {
            return prefsStorage.getLong(PROFILE_SYNC_TIME_KEY, System.currentTimeMillis())
                ?: System.currentTimeMillis()
        }
        set(value) {
            prefsStorage.saveLong(PROFILE_SYNC_TIME_KEY, value)
        }

    val isProfileUnSynced: Boolean
        get() = System.currentTimeMillis() - syncTime > PROFILE_SYNC_TIME_DIFF

    val isProfileTemp: Boolean
        get() = profile == null

    private companion object {
        const val PROFILE_ID_KEY = "PROFILE_ID"
        const val PROFILE_KEY = "PROFILE"
        const val PROFILE_SYNC_TIME_KEY = "PROFILE_SYNC_TIME"
        const val PROFILE_SYNC_TIME_DIFF = 7200000

        const val DEVICE_ID_KEY = "DEVICE_ID_KEY"
    }
}