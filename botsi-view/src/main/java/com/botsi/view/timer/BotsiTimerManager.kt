package com.botsi.view.timer

import androidx.annotation.RestrictTo
import com.botsi.view.model.content.BotsiTimerMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal interface BotsiTimerManager {
    fun startTimer(
        timerInternalId: String,
        timerId: String?,
        timerMode: BotsiTimerMode?,
        startTime: Long?,
        scope: CoroutineScope,
        onTimerUpdate: (Long) -> Unit,
        onTimerFinished: () -> Unit
    )

    fun dispose(scope: CoroutineScope)
}

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal class BotsiTimerManagerImpl(
    private val storage: BotsiTimerStorage,
    private val timerResolver: BotsiTimerResolver
) : BotsiTimerManager {

    private val activeTimers = ConcurrentHashMap<String, TimerState>()

    private data class TimerState(
        val internalTimerId: String?,
        val timerId: String?,
        val timerMode: BotsiTimerMode?,
        val startTime: Long,
        var currentValue: Long,
        var job: Job?,
        val onUpdate: (Long) -> Unit,
        val onFinished: () -> Unit
    )

    override fun startTimer(
        timerInternalId: String,
        timerId: String?,
        timerMode: BotsiTimerMode?,
        startTime: Long?,
        scope: CoroutineScope,
        onTimerUpdate: (Long) -> Unit,
        onTimerFinished: () -> Unit
    ) {
        // Stop existing timer if any
        stopTimer(timerInternalId)

        val mode = timerMode ?: BotsiTimerMode.ResetEveryTime
        val initialTime = startTime ?: 0L

        // Determine the actual start value based on timer mode
        val actualStartValue = when (mode) {
            BotsiTimerMode.ResetEveryTime -> {
                // Always start fresh
                initialTime
            }

            BotsiTimerMode.ResetEveryLaunch -> {
                // Use stored value if exists, otherwise start fresh
                storage.getTimerValue(timerInternalId, mode) ?: initialTime
            }

            BotsiTimerMode.KeepTimer -> {
                // Use persistent stored value if exists, otherwise start fresh
                storage.getTimerValue(timerInternalId, mode) ?: initialTime
            }

            BotsiTimerMode.DeveloperDefined -> {
                // Use stored value if exists, otherwise start fresh
                timerId?.let {
                    val date = timerResolver.timerEndAtDate(timerId)
                    date.time - System.currentTimeMillis()
                } ?: initialTime
            }
        }

        val timerState = TimerState(
            internalTimerId = timerInternalId,
            timerId = timerId,
            timerMode = mode,
            startTime = initialTime,
            currentValue = actualStartValue,
            job = null,
            onUpdate = onTimerUpdate,
            onFinished = onTimerFinished
        )

        activeTimers[timerInternalId] = timerState

        // Start the countdown
        startCountdown(timerInternalId, scope)
    }

    private fun startCountdown(timerInternalId: String, scope: CoroutineScope) {
        val timerState = activeTimers[timerInternalId] ?: return

        timerState.job = scope.launch {
            while (timerState.currentValue > 0) {
                timerState.onUpdate(timerState.currentValue)

                delay(1000L)

                timerState.currentValue -= 1000L
            }

            // Timer finished
            timerState.onFinished()

            // Clean up
            activeTimers.remove(timerInternalId)
        }
    }

    private fun stopTimer(timerInternalId: String) {
        activeTimers[timerInternalId]?.let { timerState ->
            timerState.job?.cancel()
            activeTimers.remove(timerInternalId)
        }
    }

    override fun dispose(scope: CoroutineScope) {
        activeTimers.forEach { entry ->
            storage.setTimerCurrentTimeValue(entry.key, entry.value.timerMode!!)
            storage.setTimerValue(entry.key, entry.value.timerMode!!, entry.value.currentValue)
        }
        activeTimers.values.forEach { it.job?.cancel() }
        activeTimers.clear()
    }
}