package com.competo.botsi.view.ui.view

import android.content.Context
import android.util.AttributeSet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.AbstractComposeView
import com.competo.botsi.view.BotsiViewConfig
import com.competo.botsi.view.isNotEmpty
import com.competo.botsi.view.ui.compose.entry_point.BotsiPaywallEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class BotsiPaywallView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AbstractComposeView(context, attrs, defStyleAttr) {

    private val viewConfigStateFlow = MutableStateFlow(BotsiViewConfig())

    fun setViewConfig(config: BotsiViewConfig) {
        viewConfigStateFlow.update { config }
    }

    @Composable
    override fun Content() {
        val viewConfig by viewConfigStateFlow.collectAsState()
        BotsiPaywallEntryPoint(viewConfig)
    }

}