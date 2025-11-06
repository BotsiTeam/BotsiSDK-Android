package com.botsi.view.timer

import androidx.annotation.RestrictTo
import com.botsi.view.model.content.BotsiTimerMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date
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

    fun timerEndAtDate(timerId: String): Date

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
        stopTimer(timerInternalId)

        val mode = timerMode ?: BotsiTimerMode.ResetEveryTime
        val initialTime = startTime ?: 0

        val actualStartValue = when (mode) {
            BotsiTimerMode.DeveloperDefined,
            BotsiTimerMode.ResetEveryTime -> {
                initialTime
            }

            BotsiTimerMode.ResetEveryLaunch -> {
                storage.getTimerValue(timerInternalId, mode)
                    ?.takeIf { it > 0 } ?: initialTime
            }

            BotsiTimerMode.KeepTimer -> {
                storage.getTimerValue(timerInternalId, mode)
                    ?.takeIf { it > 0 } ?: initialTime
            }
        }

        val timerState = TimerState(
            internalTimerId = timerInternalId,
            timerId = timerId,
            timerMode = mode,
            currentValue = actualStartValue,
            job = null,
            onUpdate = onTimerUpdate,
            onFinished = onTimerFinished
        )

        activeTimers[timerInternalId] = timerState
        startCountdown(timerInternalId, scope)
    }

    override fun timerEndAtDate(timerId: String): Date {
        return timerResolver.timerEndAtDate(timerId)
    }

    private fun startCountdown(timerInternalId: String, scope: CoroutineScope) {
        val timerState = activeTimers[timerInternalId] ?: return

        timerState.job = scope.launch {
            while (timerState.currentValue > 0) {
                timerState.onUpdate(timerState.currentValue)

                delay(1000L)

                timerState.currentValue -= 1000L
            }

            timerState.onUpdate(timerState.currentValue)
            timerState.onFinished()
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
            storage.setTimerValue(entry.key, entry.value.timerMode!!, entry.value.currentValue)
        }
        activeTimers.values.forEach { it.job?.cancel() }
        activeTimers.clear()
    }
}