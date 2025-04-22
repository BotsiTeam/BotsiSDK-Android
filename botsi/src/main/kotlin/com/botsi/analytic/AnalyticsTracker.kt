package com.botsi.analytic

import androidx.annotation.RestrictTo

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal interface AnalyticsTracker {

    fun trackEvent(event: AnalyticsEvent)

}