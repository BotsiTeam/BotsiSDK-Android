package com.botsi.view.model.content

internal data class BotsiLinksContent(
    val hasTermOfService: Boolean? = null,
    val hasPrivacyPolicy: Boolean? = null,
    val hasRestoreButton: Boolean? = null,
    val hasLoginButton: Boolean? = null,
    val termOfService: BotsiLinksText? = null,
    val privacyPolicy: BotsiLinksText? = null,
    val restoreButton: BotsiLinksText? = null,
    val loginButton: BotsiLinksText? = null,
    val style: BotsiLinksStyle? = null,
    val padding: List<Int>? = null,
    val verticalOffset: Int? = null,
    val contentLayout: BotsiLinksContentLayout? = null,
) : BotsiBlockContent

internal data class BotsiLinksText(
    val text: String? = null,
    val textFallback: String? = null,
    val url: String? = null,
    val urlFallback: String? = null,
)

internal data class BotsiLinksStyle(
    val font: BotsiFont? = null,
    val size: Float? = null,
    val color: String? = null,
    val opacity: Float? = null,
    val dividersColor: String? = null,
    val dividersOpacity: Float? = null,
    val dividersThickness: Int? = null,
)

internal data class BotsiLinksContentLayout(
    val layout: BotsiLayoutDirection? = null,
    val spacing: Int? = null,
)