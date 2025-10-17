package com.botsi.view.timer

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.RestrictTo
import androidx.core.content.edit
import com.botsi.view.model.content.BotsiTimerMode
import kotlin.math.min

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal interface BotsiTimerStorage {
    fun getTimerValue(timerId: String, timerMode: BotsiTimerMode): Long?
    fun setTimerValue(timerId: String, timerMode: BotsiTimerMode, value: Long)
    fun setTimerCurrentTimeValue(timerId: String, timerMode: BotsiTimerMode)
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

    override fun setTimerCurrentTimeValue(
        timerId: String,
        timerMode: BotsiTimerMode,
    ) {
        val key = "current_time_$timerId"
        when (timerMode) {
            BotsiTimerMode.KeepTimer -> {
                persistentPrefs.edit { putLong(key, System.currentTimeMillis()) }
            }

            else -> {}
        }
    }

    override fun clearSessionTimers() {
        sessionTimers.clear()
    }

    private fun getSavedThatTimerValue(timerId: String, timerMode: BotsiTimerMode): Long? {
        val key = "current_time_$timerId"
        return when (timerMode) {
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