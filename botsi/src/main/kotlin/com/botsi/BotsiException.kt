package com.botsi

class BotsiException(
    override val message: String?,
    override val cause: Throwable? = null,
    val code: Int = -1
) : Exception(message, cause)