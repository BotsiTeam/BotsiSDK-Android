package com.botsi.data.model.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class BotsiBaseResponse<T>(
    @SerializedName("ok")
    val status: Boolean = false,
    @SerializedName("data")
    val data: T,
)
