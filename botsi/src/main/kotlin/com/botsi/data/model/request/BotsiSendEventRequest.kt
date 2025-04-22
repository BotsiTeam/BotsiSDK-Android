package com.botsi.data.model.request

import androidx.annotation.RestrictTo
import com.botsi.analytic.AnalyticsEvent

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal class BotsiSendEventRequest(
    val events: List<AnalyticsEvent>
) {
    internal companion object {
        fun create(events: List<AnalyticsEvent>) = BotsiSendEventRequest(events)
    }
}