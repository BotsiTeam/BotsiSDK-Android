package com.botsi.view.timer

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.RestrictTo
import com.botsi.view.model.content.BotsiTimerMode
import androidx.core.content.edit

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal interface BotsiTimerStorage {
    fun getTimerValue(timerId: String, timerMode: BotsiTimerMode): Long?
    fun setTimerValue(timerId: String, timerMode: BotsiTimerMode, value: Long)
    fun removeTimerValue(timerId: String, timerMode: BotsiTimerMode)
    fun clearSessionTimers()
}

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal class BotsiTimerStorageImpl(
    private val context: Context
) : BotsiTimerStorage {

    private val persistentPrefs: SharedPreferences by lazy {
        context.getSharedPreferences("botsi_timer_persistent", Context.MODE_PRIVATE)
    }

    private val sessionTimers = mutableMapOf<String, Long>()

    override fun getTimerValue(timerId: String, timerMode: BotsiTimerMode): Long? {
        return when (timerMode) {
            BotsiTimerMode.KeepTimer -> {
                persistentPrefs.getLong(timerId, -1L).takeIf { it != -1L }
            }
            BotsiTimerMode.ResetEveryLaunch -> {
                BotsiLaunchTimerStorage.launchTimers[timerId]
            }
            BotsiTimerMode.ResetEveryTime -> {
                sessionTimers[timerId]
            }
            BotsiTimerMode.DeveloperDefined -> {
                // For developer defined, we use session storage but don't auto-reset
                sessionTimers[timerId]
            }
        }
    }

    override fun setTimerValue(timerId: String, timerMode: BotsiTimerMode, value: Long) {
        when (timerMode) {
            BotsiTimerMode.KeepTimer -> {
                persistentPrefs.edit { putLong(timerId, value) }
            }
            BotsiTimerMode.ResetEveryLaunch -> {
                BotsiLaunchTimerStorage.launchTimers[timerId] = value
            }
            BotsiTimerMode.ResetEveryTime -> {
                sessionTimers[timerId] = value
            }
            BotsiTimerMode.DeveloperDefined -> {
                sessionTimers[timerId] = value
            }
        }
    }

    override fun removeTimerValue(timerId: String, timerMode: BotsiTimerMode) {
        when (timerMode) {
            BotsiTimerMode.KeepTimer -> {
                persistentPrefs.edit { remove(timerId) }
            }
            BotsiTimerMode.ResetEveryLaunch -> {
                BotsiLaunchTimerStorage.launchTimers.remove(timerId)
            }
            BotsiTimerMode.ResetEveryTime -> {
                sessionTimers.remove(timerId)
            }
            BotsiTimerMode.DeveloperDefined -> {
                sessionTimers.remove(timerId)
            }
        }
    }

    override fun clearSessionTimers() {
        sessionTimers.clear()
    }
}