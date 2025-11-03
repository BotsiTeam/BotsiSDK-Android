package com.botsi.view.ui.compose.scroll

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.MutableState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource

internal class BotsiHeroImageOverlayNestedScroll(
    private val scrollState: LazyListState,
    private val initialOffset: Float,
    private val offsetStateFlow: MutableState<Float>,
) : NestedScrollConnection {

    override fun onPreScroll(
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        if (scrollState.canScrollBackward) {
            offsetStateFlow.value = 0f
            return Offset.Zero
        }

        val delta = available.y
        val currentOffset = offsetStateFlow.value
        val newOffset = currentOffset + delta
        if (newOffset < 0f || initialOffset < 0f) return Offset.Zero
        val clampedOffset = newOffset.coerceIn(0f, initialOffset)
        val consumed = clampedOffset - currentOffset
        offsetStateFlow.value = clampedOffset
        return Offset(0f, consumed)
    }
}