package com.botsi.view.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.botsi.Botsi
import com.botsi.domain.model.BotsiPaywall
import com.botsi.domain.model.BotsiProduct
import com.botsi.view.BotsiViewConfig
import com.botsi.view.handler.BotsiPublicEventHandler
import com.botsi.view.timer.BotsiTimerResolver
import com.botsi.view.ui.compose.entry_point.BotsiPaywallEntryPoint

class BotsiViewFragment : Fragment() {

    private var paywall: BotsiPaywall? = null
    private var products: List<BotsiProduct>? = null
    private var eventHandler: BotsiPublicEventHandler? = null
    private var timerResolver: BotsiTimerResolver? = null

    /**
     * Default implementation of BotsiPublicClickHandler with basic functionality
     * for handling different click actions in the Botsi paywall.
     */

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(context = requireContext()).apply {
            setContent {
                MaterialTheme {
                    BotsiPaywallEntryPoint(
                        activity = requireActivity(),
                        viewConfig = BotsiViewConfig(
                            paywall = paywall,
                            products = products,
                        ),
                        eventHandler = eventHandler,
                        timerResolver = timerResolver ?: BotsiTimerResolver.default
                    )
                }
            }
        }
    }

    /**
     * Set the paywall ID for this fragment
     */
    fun setPaywall(paywall: BotsiPaywall?) {
        this.paywall = paywall
    }

    fun setProducts(products: List<BotsiProduct>?) {
        this.products = products
    }

    /**
     * Set a custom event handler for this fragment
     */
    fun setEventHandler(clickHandler: BotsiPublicEventHandler?) {
        this.eventHandler = clickHandler
    }

    fun setTimerResolver(timerResolver: BotsiTimerResolver) {
        this.timerResolver = timerResolver
    }

    companion object {
        /**
         * Create a new instance of BotsiViewFragment
         */
        fun newInstance(
            paywall: BotsiPaywall?,
            products: List<BotsiProduct>?,
            clickHandler: BotsiPublicEventHandler? = null,
            timerResolver: BotsiTimerResolver = BotsiTimerResolver.default
        ): BotsiViewFragment {
            return BotsiViewFragment().apply {
                setPaywall(paywall)
                setProducts(products)
                setEventHandler(clickHandler)
                setTimerResolver(timerResolver)
            }
        }
    }
}
