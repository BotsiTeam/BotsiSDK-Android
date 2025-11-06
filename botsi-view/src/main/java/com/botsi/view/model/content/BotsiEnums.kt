package com.botsi.view.model.content

import androidx.annotation.Keep

@Keep
internal enum class BotsiTimerMode(val key: String) {
    ResetEveryTime("Reset timer on every paywall view"),
    ResetEveryLaunch("Reset timer on every app launch"),
    KeepTimer("Keep timer across app launches"),
    DeveloperDefined("Developer defined");

    companion object {
        fun findByKey(key: String): BotsiTimerMode =
            entries.find { it.key == key } ?: ResetEveryTime
    }
}

@Keep
internal enum class BotsiAlign {
    Left,
    Right,
    Center,
    Top,
    Bottom,
    Column,
}

@Keep
internal enum class BotsiButtonIconType {
    None,
    Close,
    Prev,
    Next,
}

@Keep
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

@Keep
internal enum class BotsiHeroImageContentStyle {
    Overlay,
    Transparent,
    Flat,
}

@Keep
internal enum class BotsiCarouselLastOption(val key: String) {
    StartOver("start over"),
    Stop("stop slide show");

    companion object {
        fun findByKey(key: String): BotsiCarouselLastOption =
            entries.find { it.key == key } ?: Stop
    }
}

@Keep
internal enum class BotsiCarouselInteractive(val key: String) {
    WithoutAffect("without_affect"),
    Stop("stop"),
    Pause("pause");

    companion object {
        fun findByKey(key: String): BotsiCarouselInteractive =
            entries.find { it.key == key } ?: WithoutAffect
    }
}

@Keep
internal enum class BotsiButtonType {
    Icon,
    Text,
}

@Keep
internal enum class BotsiFontStyleType {
    Normal,
    Italic,
    Bold,
}

@Keep
internal enum class BotsiOnOverflowBehavior {
    Scale,
}

@Keep
internal enum class BotsiImageAspect {
    Fill,
    Fit,
    Stretch,
}

@Keep
internal enum class BotsiDefaultIcon {
    Tick,
    Checkmark,
    Dot,
}

@Keep
internal enum class BotsiLayoutDirection {
    Vertical,
    Horizontal,
}

@Keep
internal enum class BotsiProductState {
    Default,
    Selected,
}

@Keep
internal enum class BotsiTabState {
    Active,
    Inactive,
}

@Keep
internal enum class BotsiCarouselPageControlType {
    Overlay,
    Outside,
}

@Keep
internal enum class BotsiTimerFormat(val format: String) {
    Ss("ss"),
    MmSs("mm ss"),
    HhMmSs("hh mm ss"),
    DdHhMmSs("dd hh mm ss");

    companion object {
        fun findByFormat(key: String): BotsiTimerFormat? =
            BotsiTimerFormat.entries.find { it.format == key }
    }
}

@Keep
internal enum class BotsiTimerSeparator(val symbol: String) {
    Colon(":"),
    Dash("-"),
    Space(" "),
    Letter("D H M S")
}

@Keep
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
    Toggle("toggle_control"),
    ToggleOn("toggle_on"),
    ToggleOff("toggle_off"),
    Image("image"),
    TabControl("tab_control"),
    TabGroup("tab_group"),
    MainPlans("main_plans"),
    MorePlans("more_plans"),
    MorePlansSheet("more_plans_sheet"),
    PlansControl("plans_control");

    companion object {
        fun findByKey(key: String): BotsiContentType? = entries.find { it.key == key }
    }
}

@Keep
sealed interface BotsiButtonAction {
    data object None : BotsiButtonAction
    data object Close : BotsiButtonAction
    data object Login : BotsiButtonAction
    data object Restore : BotsiButtonAction
    data object Custom : BotsiButtonAction
    data object Purchase : BotsiButtonAction

    // internal usage
    data class Link(val url: String) : BotsiButtonAction

    companion object {
        fun valueOf(value: String): BotsiButtonAction =
            when (value) {
                "Close" -> Close
                "Login" -> Login
                "Restore" -> Restore
                "Custom" -> Custom
                else -> {
                    if (value.lowercase().contains("purchase")) {
                        Purchase
                    } else {
                        None
                    }
                }
            }
    }
}
