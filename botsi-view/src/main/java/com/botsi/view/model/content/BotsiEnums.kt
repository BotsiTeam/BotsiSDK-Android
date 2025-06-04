package com.botsi.view.model.content

internal enum class BotsiAlign {
    Left,
    Right,
    Center,
}

internal enum class BotsiButtonAction {
    None,
    Close,
    Custom,
}

internal enum class BotsiButtonIconType {
    None,
    Close,
}

internal enum class BotsiShape {
    Rectangle,
}

internal enum class BotsiContentStyle {
    Overlay,
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

internal enum class BotsiContentType(val key: String) {
    Layout("layout"),
    HeroImage("hero_image"),
    Text("text"),
    List("list"),
    ListNested("list_nested"),
    Footer("footer"),
    Products("products"),
    ProductItem("product_item"),
    Button("button");

    companion object {
        fun findByKey(key: String): BotsiContentType? = entries.find { it.key == key }
    }
}