package com.botsi.example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.botsi.ai.ui.composables.BotsiAiEntryPoint
import com.botsi.ai.ui.composables.Paywall3Ui

class BotsiAiFragment : Fragment() {

    companion object {
        fun newInstance() = BotsiAiFragment()
    }

    private val app: BotsiApp
        get() = requireContext().applicationContext as BotsiApp

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(context = requireContext()).apply {
            setContent {
                MaterialTheme {
                    BotsiAiEntryPoint(
                        activity = requireActivity(),
                        onBack = {
                            activity?.supportFragmentManager?.popBackStack()
                        }
                    )
                }
            }
        }
    }
}