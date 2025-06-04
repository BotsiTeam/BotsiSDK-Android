package com.botsi.view.di

import androidx.annotation.RestrictTo
import com.botsi.view.delegate.BotsiPaywallDelegate
import com.botsi.view.delegate.BotsiPaywallDelegateImpl
import com.botsi.view.mapper.BotsiBlockMetaMapper
import com.botsi.view.mapper.BotsiButtonContentMapper
import com.botsi.view.mapper.BotsiButtonStyleMapper
import com.botsi.view.mapper.BotsiFontMapper
import com.botsi.view.mapper.BotsiFooterContentMapper
import com.botsi.view.mapper.BotsiHeroImageContentMapper
import com.botsi.view.mapper.BotsiLayoutContentMapper
import com.botsi.view.mapper.BotsiListContentMapper
import com.botsi.view.mapper.BotsiListNestedContentMapper
import com.botsi.view.mapper.BotsiPaywallBlocksMapper
import com.botsi.view.mapper.BotsiProductItemContentMapper
import com.botsi.view.mapper.BotsiProductsContentMapper
import com.botsi.view.mapper.BotsiTextContentMapper
import com.botsi.view.mapper.BotsiTextMapper

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class BotsiPaywallDIManager {
    private val dependencies = mutableMapOf<Class<*>, Any>()

    init {
        with(dependencies) {
            put(BotsiFontMapper::class.java, BotsiFontMapper())
            put(BotsiButtonStyleMapper::class.java, BotsiButtonStyleMapper())
            put(BotsiBlockMetaMapper::class.java, BotsiBlockMetaMapper())
            put(BotsiFooterContentMapper::class.java, BotsiFooterContentMapper())
            put(
                BotsiTextMapper::class.java, BotsiTextMapper(
                    fontMapper = inject()
                )
            )
            put(BotsiHeroImageContentMapper::class.java, BotsiHeroImageContentMapper())
            put(
                BotsiButtonContentMapper::class.java,
                BotsiButtonContentMapper(
                    buttonStyleMapper = inject(),
                    textMapper = inject()
                )
            )
            put(
                BotsiLayoutContentMapper::class.java, BotsiLayoutContentMapper(
                    fontMapper = inject(),
                    buttonStyleMapper = inject(),
                    textMapper = inject(),
                )
            )
            put(BotsiListContentMapper::class.java, BotsiListContentMapper())
            put(BotsiListNestedContentMapper::class.java, BotsiListNestedContentMapper())
            put(BotsiProductItemContentMapper::class.java, BotsiProductItemContentMapper())
            put(BotsiProductsContentMapper::class.java, BotsiProductsContentMapper())
            put(
                BotsiTextContentMapper::class.java, BotsiTextContentMapper(
                    textMapper = inject()
                )
            )
            put(
                BotsiPaywallBlocksMapper::class.java, BotsiPaywallBlocksMapper(
                    blockMetaMapper = inject(),
                    layoutContentMapper = inject(),
                    heroImageContentMapper = inject(),
                    textContentMapper = inject(),
                    listContentMapper = inject(),
                    footerContentMapper = inject(),
                    productsContentMapper = inject(),
                    productItemContentMapper = inject(),
                    listNestedContentMapper = inject(),
                    buttonContentMapper = inject(),
                )
            )
            put(
                BotsiPaywallDelegate::class.java, BotsiPaywallDelegateImpl(
                    paywallBlocksMapper = inject()
                )
            )
        }
    }

    inline fun <reified T> inject(): T {
        return dependencies[T::class.java] as T
    }

}