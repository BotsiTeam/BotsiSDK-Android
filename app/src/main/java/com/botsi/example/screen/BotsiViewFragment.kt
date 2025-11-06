package com.botsi.example.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.botsi.domain.model.BotsiPaywall
import com.botsi.domain.model.BotsiProduct
import com.botsi.domain.model.BotsiProfile
import com.botsi.domain.model.BotsiPurchase
import com.botsi.view.BotsiViewConfig
import com.botsi.view.handler.BotsiPublicEventHandler
import com.botsi.view.timer.BotsiTimerResolver
import com.botsi.view.ui.compose.entry_point.BotsiPaywallEntryPoint
import java.util.Date

class BotsiViewFragment : Fragment() {

    private var paywall: BotsiPaywall? = null
    private var products: List<BotsiProduct>? = null

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
                        viewConfig = BotsiViewConfig(
                            paywall = paywall,
                            products = products,
                        ),
                        timerResolver = object : BotsiTimerResolver{
                            override fun timerEndAtDate(timerId: String): Date {
                                val durationMillis = parseTimeString(timerId)
                                return Date(System.currentTimeMillis() + durationMillis)
                            }

                            private fun parseTimeString(timeString: String): Long {
                                if (timeString.isBlank()) return 0L

                                return try {
                                    val cleanedTime = timeString.trim().lowercase()
                                    var totalMilliseconds = 0L

                                    // Extract days (e.g., "1d")
                                    val dayPattern = Regex("""(\d+)d""")
                                    dayPattern.find(cleanedTime)?.let { match ->
                                        val days = match.groupValues[1].toInt()
                                        totalMilliseconds += days * 86400 * 1000L
                                    }

                                    // Extract hours (e.g., "10h")
                                    val hourPattern = Regex("""(\d+)h""")
                                    hourPattern.find(cleanedTime)?.let { match ->
                                        val hours = match.groupValues[1].toInt()
                                        totalMilliseconds += hours * 3600 * 1000L
                                    }

                                    // Extract minutes (e.g., "12m")
                                    val minutePattern = Regex("""(\d+)m""")
                                    minutePattern.find(cleanedTime)?.let { match ->
                                        val minutes = match.groupValues[1].toInt()
                                        totalMilliseconds += minutes * 60 * 1000L
                                    }

                                    // Extract seconds (e.g., "10s")
                                    val secondPattern = Regex("""(\d+)s""")
                                    secondPattern.find(cleanedTime)?.let { match ->
                                        val seconds = match.groupValues[1].toInt()
                                        totalMilliseconds += seconds * 1000L
                                    }

                                    // If no time units found, try to parse as plain seconds
                                    if (totalMilliseconds == 0L) {
                                        val seconds = cleanedTime.toIntOrNull()
                                        if (seconds != null && seconds > 0) {
                                            totalMilliseconds = seconds * 1000L
                                        }
                                    }

                                    totalMilliseconds
                                } catch (e: Exception) {
                                    // Default to 1 hour if parsing fails
                                    3600 * 1000L
                                }
                            }
                        },
                        eventHandler = object : BotsiPublicEventHandler {
                            override fun onLoginAction() {
                                showToaster("Login action clicked")
                            }

                            override fun onCustomAction(actionId: String) {
                                showToaster("$actionId clicked")
                            }

                            override fun onSuccessRestore(profile: BotsiProfile) {
                                showToaster("Restore success")
                            }

                            override fun onErrorRestore(error: Throwable) {
                                showToaster("Restore error ${error.message}")
                            }

                            override fun onSuccessPurchase(
                                profile: BotsiProfile,
                                purchase: BotsiPurchase
                            ) {
                                showToaster("Purchase success")
                            }

                            override fun onErrorPurchase(error: Throwable) {
                                showToaster("Purchase error ${error.message}")
                            }

                            override fun onTimerEnd(actionId: String) {
                                showToaster("Timer end $actionId")
                            }

                        },
                    )
                }
            }
        }
    }

    private fun showToaster(message: String) {
        requireActivity().runOnUiThread {
            Toast.makeText(
                requireContext(),
                message,
                Toast.LENGTH_SHORT
            ).show()
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

    companion object {
        /**
         * Create a new instance of BotsiViewFragment
         */
        fun newInstance(
            paywall: BotsiPaywall?,
            products: List<BotsiProduct>?,
        ): BotsiViewFragment {
            return BotsiViewFragment().apply {
                setPaywall(paywall)
                setProducts(products)
            }
        }
    }
}
