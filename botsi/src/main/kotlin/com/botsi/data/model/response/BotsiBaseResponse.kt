package com.botsi.data.model.response

import com.google.gson.annotations.SerializedName

data class BotsiBaseResponse<T>(
    @SerializedName("ok")
    val status: Boolean = false,
    @SerializedName("data")
    val data: T,
)