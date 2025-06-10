package com.botsi.view.model.content

import com.botsi.view.model.content.BotsiHeroImageShape.Rectangle

internal enum class BotsiAlign {
    Left,
    Right,
    Center,
    Top,
    Bottom,
}

internal enum class BotsiButtonIconType {
    None,
    Close,
    Prev,
    Next,
}

internal enum class BotsiHeroImageShape(val key: String) {
    Rectangle("rectangle"),
    Circle("circle"),
    Leaf("leaf"),
    RoundedRectangle("rounded_rectangle"),
    ConvexMask("convex_mask"),
    ConcaveMask("concave_mask");

    companion object {
        fun findByKey(key: String): BotsiHeroImageShape =
            entries.find { it.key == key } ?: Rectangle
    }
}

internal enum class BotsiHeroImageContentStyle {
    Overlay,
    Transparent,
    Flat,
}

internal enum class BotsiCarouselLastOption(val key: String) {
    StartOver("start over"),
    Stop("stop slide show");

    companion object {
        fun findByKey(key: String): BotsiCarouselLastOption =
            entries.find { it.key == key } ?: Stop
    }
}

internal enum class BotsiCarouselInteractive(val key: String) {
    WithoutAffect("without_affect"),
    Stop("stop"),
    Pause("pause");

    companion object {
        fun findByKey(key: String): BotsiCarouselInteractive =
            entries.find { it.key == key } ?: WithoutAffect
    }
}

internal enum class BotsiButtonType {
    Icon,
    Text,
}

internal enum class BotsiFontStyleType {
    Normal,
    Italic,
    Bold,
}

internal enum class BotsiOnOverflowBehavior {
    Scale,
}

internal enum class BotsiImageAspect {
    Fill,
    Fit,
    Stretch,
}

internal enum class BotsiDefaultIcon {
    Tick,
    Checkmark,
    Dot,
}

internal enum class BotsiLayoutDirection {
    Vertical,
    Horizontal,
}

internal enum class BotsiCarouselPageControlType {
    Overlay,
    Outside,
}

internal enum class BotsiTimerFormat(val format: String) {
    Ss("ss"),
    MmSs("mm ss"),
    HhMmSs("hh mm ss"),
    DdHhMmSs("dd hh mm ss");

    companion object {
        fun findByFormat(key: String): BotsiTimerFormat? = BotsiTimerFormat.entries.find { it.format == key }
    }
}

internal enum class BotsiTimerSeparator {
    Colon,
    Dash,
    Space,
    Letter
}

internal enum class BotsiContentType(val key: String) {
    Layout("layout"),
    HeroImage("hero_image"),
    Text("text"),
    List("list"),
    ListNested("list_nested"),
    Footer("footer"),
    Products("products"),
    ProductItem("product_item"),
    Button("button"),
    Card("card"),
    Links("links"),
    Carousel("carousel"),
    Timer("timer"),
    Image("image");

    companion object {
        fun findByKey(key: String): BotsiContentType? = entries.find { it.key == key }
    }
}


internal sealed interface BotsiButtonAction {
    data object None : BotsiButtonAction
    data object Close : BotsiButtonAction
    data object Login : BotsiButtonAction
    data object Restore : BotsiButtonAction
    data object Custom : BotsiButtonAction

    // internal usage
    data class Link(val url: String) : BotsiButtonAction

    companion object {
        fun valueOf(value: String): BotsiButtonAction =
            when (value) {
                "Close" -> Close
                "Login" -> Login
                "Restore" -> Restore
                "Custom" -> Custom
                else -> None
            }
    }
}