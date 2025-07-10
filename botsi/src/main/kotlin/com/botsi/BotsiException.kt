package com.botsi

import androidx.annotation.Keep

@Keep
class BotsiException(
    override val message: String?,
    override val cause: Throwable? = null,
    val code: Int = -1
) : Exception(message, cause)