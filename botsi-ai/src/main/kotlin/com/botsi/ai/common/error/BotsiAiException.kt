package com.botsi.ai.common.error

class BotsiAiException(
    override val message: String?,
    override val cause: Throwable? = null,
    val code: Int = -1
) : Exception(message, cause)