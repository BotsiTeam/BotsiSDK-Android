package com.botsi.view.mapper

import com.botsi.view.model.content.BotsiBlockMeta
import com.botsi.view.model.content.BotsiContentType
import com.botsi.view.model.content.BotsiPaywallBlock
import com.botsi.view.model.content.BotsiPaywallContentStructure
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class BotsiPaywallBlocksMapper(
    private val blockMetaMapper: BotsiBlockMetaMapper,
    private val layoutContentMapper: BotsiLayoutContentMapper,
    private val heroImageContentMapper: BotsiHeroImageContentMapper,
    private val textContentMapper: BotsiTextContentMapper,
    private val listContentMapper: BotsiListContentMapper,
    private val footerContentMapper: BotsiFooterContentMapper,
    private val productsContentMapper: BotsiProductsContentMapper,
    private val productItemContentMapper: BotsiProductItemContentMapper,
    private val listNestedContentMapper: BotsiListNestedContentMapper,
    private val buttonContentMapper: BotsiButtonContentMapper,
    private val imageContentMapper: BotsiImageContentMapper,
    private val cardContentMapper: BotsiCardContentMapper,
    private val linksContentMapper: BotsiLinksContentMapper,
    private val carouselContentMapper: BotsiCarouselContentMapper,
    private val timerContentMapper: BotsiTimerContentMapper,
    private val productToggleContentMapper: BotsiProductToggleContentMapper,
    private val productToggleStateContentMapper: BotsiProductToggleStateContentMapper,
    private val tabControlContentMapper: BotsiTabControlContentMapper,
    private val tabGroupContentMapper: BotsiTabGroupContentMapper,
) {

    suspend fun map(jsonElement: JsonElement): BotsiPaywallContentStructure {
        return withContext(Dispatchers.Default) {
            val blocks = mapBlocks(jsonElement)

            BotsiPaywallContentStructure(
                layout = blocks.firstOrNull { it.meta?.type == BotsiContentType.Layout },
                content = blocks.filter {
                    it.meta?.type != BotsiContentType.Layout ||
                            it.meta.type != BotsiContentType.Footer ||
                            it.meta.type != BotsiContentType.HeroImage ||
                            it.meta.type != BotsiContentType.HeroImage

                },
                footer = blocks.firstOrNull { it.meta?.type == BotsiContentType.Footer },
                heroImage = blocks.firstOrNull { it.meta?.type == BotsiContentType.HeroImage }
            )
        }
    }

    private suspend fun mapBlocks(jsonElement: JsonElement): List<BotsiPaywallBlock> {
        return runCatching { jsonElement.asJsonObject.get("data").asJsonArray }
            .recoverCatching { jsonElement.asJsonObject.get("children").asJsonArray }
            .getOrDefault(JsonArray())
            .mapNotNull { json ->
                val meta = runCatching { blockMetaMapper.map(json.asJsonObject.get("meta")) }.getOrNull()
                val contentJson = runCatching { json.asJsonObject.get("content") }.getOrNull()
                contentJson?.let {
                    BotsiPaywallBlock(
                        meta = meta,
                        content = when (meta?.type) {
                            BotsiContentType.Layout -> layoutContentMapper.map(contentJson)
                            BotsiContentType.HeroImage -> heroImageContentMapper.map(contentJson)
                            BotsiContentType.Text -> textContentMapper.map(contentJson)
                            BotsiContentType.List -> listContentMapper.map(contentJson)
                            BotsiContentType.ListNested -> listNestedContentMapper.map(contentJson)
                            BotsiContentType.Footer -> footerContentMapper.map(contentJson)
                            BotsiContentType.Products -> productsContentMapper.map(contentJson)
                            BotsiContentType.ProductItem -> productItemContentMapper.map(contentJson)
                            BotsiContentType.Button -> buttonContentMapper.map(contentJson)
                            BotsiContentType.Image -> imageContentMapper.map(contentJson)
                            BotsiContentType.Card -> cardContentMapper.map(contentJson)
                            BotsiContentType.Links -> linksContentMapper.map(contentJson)
                            BotsiContentType.Carousel -> carouselContentMapper.map(contentJson)
                            BotsiContentType.Timer -> timerContentMapper.map(contentJson)
                            BotsiContentType.Toggle -> productToggleContentMapper.map(contentJson)
                            BotsiContentType.ToggleOn,
                            BotsiContentType.ToggleOff -> productToggleStateContentMapper.map(contentJson)
                            BotsiContentType.TabControl -> tabControlContentMapper.map(contentJson)
                            BotsiContentType.TabGroup -> tabGroupContentMapper.map(contentJson)
//                                "localization" -> buttonContentMapper.map(contentJson)
                            else -> null
                        },
                        children = mapBlocks(json)
                    )
                }
            }
    }
}
