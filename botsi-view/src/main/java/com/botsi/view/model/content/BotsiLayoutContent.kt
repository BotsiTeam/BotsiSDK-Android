package com.botsi.view.model.content

internal data class BotsiLayoutContent(
    val darkMode: Boolean? = null,
    val purchaseFlow: String? = null,
    val backgroundColor: BotsiBackgroundColor? = null,
    val defaultFont: BotsiFont? = null,
    val contentLayout: BotsiContentLayout? = null,
    val topButtons: List<BotsiTopButton>? = null,
    val template: BotsiTemplate? = null
) : BotsiBlockContent


internal data class BotsiBackgroundColor(
    val background: String? = null,
    val opacity: Float? = null
)

internal data class BotsiContentLayout(
    val margin: List<Int>? = null,
    val spacing: Int? = null
)

internal data class BotsiTopButton(
    val action: BotsiButtonAction? = null,
    val enabled: Boolean? = null,
    val actionId: String? = null,
    val buttonType: BotsiButtonType? = null,
    val buttonAlign: BotsiAlign? = null,
    val delay: Long? = null,
    val style: BotsiButtonStyle? = null,
    val text: BotsiText? = null,
    val icon: BotsiIconStyle? = null
)

internal data class BotsiIconStyle(
    val type: BotsiButtonIconType? = null,
    val color: String? = null,
    val opacity: Float? = null
)

internal data class BotsiTemplate(
    val image: String? = null,
    val name: String? = null,
    val id: String? = null
)