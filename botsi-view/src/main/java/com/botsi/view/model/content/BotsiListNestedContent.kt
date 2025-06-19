package com.botsi.view.model.content

internal data class BotsiListNestedContent(
    val icon: String? = null,
    val connectorThickness: Int? = null,
    val connectorColor: String? = null,
    val connectorOpacity: Int? = null,
    val titleText: String? = null,
    val titleTextFallback: String? = null,
    val titleTextStyle: BotsiText? = null,
    val captionText: String? = null,
    val captionTextFallback: String? = null,
    val captionTextStyle: BotsiText? = null
) : BotsiBlockContent