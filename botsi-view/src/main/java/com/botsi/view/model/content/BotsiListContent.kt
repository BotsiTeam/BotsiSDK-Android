package com.botsi.view.model.content

internal data class BotsiListContent(
    val padding: List<Int>? = null,
    val verticalOffset: Int? = null,
    val itemSpacing: Int? = null,
    val textSpacing: Int? = null,
    val width: Int? = null,
    val height: Int? = null,
    val defaultIcon: BotsiDefaultIcon? = null,
    val iconPlacement: BotsiAlign? = null,
    val defaultColor: String? = null,
    val defaultOpacity: Float? = null,
    val connectorThickness: Int? = null,
    val connectorColor: String? = null,
    val connectorOpacity: Float? = null,
    val titleTextStyle: BotsiText? = null,
    val captionTextStyle: BotsiText? = null
) : BotsiBlockContent