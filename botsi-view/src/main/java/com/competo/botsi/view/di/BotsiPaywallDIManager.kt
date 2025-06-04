package com.competo.botsi.view.di

import androidx.annotation.RestrictTo
import com.competo.botsi.view.delegate.BotsiPaywallDelegate
import com.competo.botsi.view.delegate.BotsiPaywallDelegateImpl
import com.competo.botsi.view.mapper.BotsiLayoutContentMapper

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class BotsiPaywallDIManager {
    private val dependencies = mutableMapOf<Class<*>, Any>()

    init {
        with(dependencies) {
            put(
                BotsiLayoutContentMapper::class.java, BotsiLayoutContentMapper()
            )
            put(
                BotsiPaywallDelegate::class.java, BotsiPaywallDelegateImpl(
                    contentMapper = inject()
                )
            )
        }
    }

    inline fun <reified T> inject(): T {
        return dependencies[T::class.java] as T
    }

}