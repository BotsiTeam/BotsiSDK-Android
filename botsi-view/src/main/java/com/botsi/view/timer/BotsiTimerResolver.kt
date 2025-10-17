package com.botsi.view.timer

import java.util.Date

/**
 * Implement this interface to to use custom timer functionality
 */
fun interface BotsiTimerResolver {
    /**
     * A function that returns the date the timer with [timerId] should end at.
     *
     * @param[timerId] ID of the timer.
     *
     * @return The date the timer with [timerId] should end at.
     */
    fun timerEndAtDate(timerId: String): Date

    companion object {
        val default = object : BotsiTimerResolver {
            override fun timerEndAtDate(timerId: String): Date {
                return Date()
            }
        }
    }
}