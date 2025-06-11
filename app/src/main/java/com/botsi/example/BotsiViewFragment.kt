package com.botsi.example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.botsi.Botsi
import com.botsi.view.BotsiViewConfig
import com.botsi.view.ui.compose.entry_point.BotsiPaywallEntryPoint

class BotsiViewFragment : Fragment() {

    private val app: BotsiApp
        get() = requireContext().applicationContext as BotsiApp

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ComposeView(context = requireContext()).apply {
            setContent {
                MaterialTheme {
                    BotsiPaywallEntryPoint(
                        BotsiViewConfig(
                            paywallId = app.botsiStorage.paywallId,
                        )
                    )
                }
            }
        }
    }

    companion object {
        fun newInstance() = BotsiViewFragment()
    }
}