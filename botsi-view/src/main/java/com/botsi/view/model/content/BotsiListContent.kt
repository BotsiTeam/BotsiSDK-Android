package com.botsi.view.model.content

internal data class BotsiListContent(
    val padding: String? = null,
    val verticalOffset: Int? = null,
    val itemSpacing: Int? = null,
    val textSpacing: Int? = null,
    val width: Int? = null,
    val height: Int? = null,
    val defaultIcon: String? = null,
    val iconPlacement: String? = null,
    val defaultColor: String? = null,
    val defaultOpacity: Int? = null,
    val connectorThickness: Int? = null,
    val connectorColor: String? = null,
    val connectorOpacity: Int? = null,
    val titleTextStyle: BotsiText? = null,
    val captionTextStyle: BotsiText? = null
) : BotsiBlockContent