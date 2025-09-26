package com.botsi.ai.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
class BotsiAiResponse<T>(
    @SerializedName("ok") val status: Boolean,
    @SerializedName("data") val data: T,
)