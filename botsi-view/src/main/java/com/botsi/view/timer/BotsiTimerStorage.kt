package com.botsi.view.timer

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.RestrictTo
import androidx.core.content.edit
import com.botsi.view.model.content.BotsiTimerMode

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal interface BotsiTimerStorage {
    fun getTimerValue(timerId: String, timerMode: BotsiTimerMode): Long?
    fun setTimerValue(timerId: String, timerMode: BotsiTimerMode, value: Long)
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
            BotsiTimerMode.DeveloperDefined,
            BotsiTimerMode.KeepTimer -> {
                val diff = getTimeDiff(timerId, timerMode)
                persistentPrefs.getLong(timerId, 0)
                    .run { this - diff }
                    .takeIf { it != 0L }
            }

            BotsiTimerMode.ResetEveryLaunch -> {
                val diff = getTimeDiff(timerId, timerMode)
                BotsiLaunchTimerStorage.launchTimers[timerId]
                    ?.run { this - diff }
                    ?.takeIf { it != 0L }
            }

            BotsiTimerMode.ResetEveryTime -> {
                sessionTimers[timerId]
            }
        }
    }

    override fun setTimerValue(timerId: String, timerMode: BotsiTimerMode, value: Long) {
        when (timerMode) {
            BotsiTimerMode.DeveloperDefined,
            BotsiTimerMode.KeepTimer -> {
                persistentPrefs.edit { putLong(timerId, value) }
            }

            BotsiTimerMode.ResetEveryLaunch -> {
                BotsiLaunchTimerStorage.launchTimers[timerId] = value
            }

            BotsiTimerMode.ResetEveryTime -> {
                sessionTimers[timerId] = value
            }
        }
    }

    private fun getSavedThatTimerValue(timerId: String, timerMode: BotsiTimerMode): Long? {
        val key = "current_time_$timerId"
        return when (timerMode) {
            BotsiTimerMode.DeveloperDefined,
            BotsiTimerMode.KeepTimer -> {
                persistentPrefs.getLong(key, -1L).takeIf { it != -1L }
            }

            BotsiTimerMode.ResetEveryLaunch -> {
                BotsiLaunchTimerStorage.launchTimers[key]
            }

            else -> 0
        }
    }

    private fun getTimeDiff(key: String, mode: BotsiTimerMode): Long {
        val savedThatTime = getSavedThatTimerValue(key, mode) ?: 0
        val currentTime = System.currentTimeMillis()
        return currentTime - savedThatTime
    }

}