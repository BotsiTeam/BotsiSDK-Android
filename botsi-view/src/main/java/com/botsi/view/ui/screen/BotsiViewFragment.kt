package com.botsi.view.ui.screen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.botsi.Botsi
import com.botsi.domain.model.BotsiPaywall
import com.botsi.domain.model.BotsiProduct
import com.botsi.view.BotsiViewConfig
import com.botsi.view.handler.BotsiActionType
import com.botsi.view.handler.BotsiClickHandler
import com.botsi.view.handler.BotsiPublicEventHandler
import com.botsi.view.ui.compose.entry_point.BotsiPaywallEntryPoint

class BotsiViewFragment : Fragment() {

    private var paywall: BotsiPaywall? = null
    private var products: List<BotsiProduct>? = null
    private var eventHandler: BotsiPublicEventHandler? = null

    /**
     * Default implementation of BotsiPublicClickHandler with basic functionality
     * for handling different click actions in the Botsi paywall.
     */
    private val defaultClickHandler = object : BotsiClickHandler {

        override fun onButtonClick(actionType: BotsiActionType, actionId: String?, url: String?) {
            when (actionType) {
                BotsiActionType.Close -> {
                    parentFragmentManager.popBackStack()
                }

                BotsiActionType.Login -> {
                    eventHandler?.onLoginAction()
                }

                BotsiActionType.Restore -> {
                    eventHandler?.onRestoreAction()
                }

                BotsiActionType.Custom -> {
                    onCustomAction(actionId.orEmpty())
                }

                BotsiActionType.Link -> {
                    // Handle link action
                    url?.let { onLinkClick(it) }
                }

                BotsiActionType.None -> {
                }
            }
        }

        override fun onTopButtonClick(actionType: BotsiActionType, actionId: String?) {
            when (actionType) {
                BotsiActionType.Close -> {
                    parentFragmentManager.popBackStack()
                }

                else -> {
                    onButtonClick(actionType, actionId)
                }
            }
        }

        override fun onLinkClick(url: String) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Failed to open link", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onCustomAction(actionId: String, actionLabel: String?) {
            eventHandler?.onCustomAction(actionId, actionLabel)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ComposeView(context = requireContext()).apply {
            setContent {
                MaterialTheme {
                    LaunchedEffect(Unit) {
                        paywall?.let {
                            Botsi.logShowPaywall(it)
                        }
                    }
                    BotsiPaywallEntryPoint(
                        viewConfig = BotsiViewConfig(
                            paywallId = paywall?.id ?: 0L,
                        ),
                        clickHandler = defaultClickHandler
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

    companion object {
        /**
         * Create a new instance of BotsiViewFragment
         */
        fun newInstance(
            paywall: BotsiPaywall?,
            products: List<BotsiProduct>?,
            clickHandler: BotsiPublicEventHandler? = null
        ): BotsiViewFragment {
            return BotsiViewFragment().apply {
                setPaywall(paywall)
                setProducts(products)
                setEventHandler(clickHandler)
            }
        }
    }
}
