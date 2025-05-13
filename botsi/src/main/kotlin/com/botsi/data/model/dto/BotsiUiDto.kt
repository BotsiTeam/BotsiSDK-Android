package com.botsi.data.model.dto

import androidx.annotation.RestrictTo
import com.google.gson.annotations.SerializedName

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal data class BotsiUiBlockDto(
    @SerializedName("meta") val meta: BotsiUiMetaDto? = null,
    @SerializedName("content") val content: BotsiUiContentDto? = null,
    @SerializedName("children") val children: List<BotsiUiBlockDto>? = null
)

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal data class BotsiUiMetaDto(
    @SerializedName("block_name") val blockName: String? = null,
    @SerializedName("id") val id: String? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("icon") val icon: String? = null,
    @SerializedName("parentId") val parentId: String? = null
)

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal data class BotsiUiContentDto(
    @SerializedName("dark_mode") val darkMode: Boolean? = null,
    @SerializedName("purchase_flow") val purchaseFlow: String? = null,
    @SerializedName("background_color") val backgroundColor: BotsiUiBackgroundColorDto? = null,
    @SerializedName("default_font") val defaultFont: BotsiUiTextFontDto? = null,
    @SerializedName("content_layout") val contentLayout: BotsiUiContentLayoutDto? = null,
    @SerializedName("top_buttons") val topButtons: List<TopButton>? = null,
    @SerializedName("template") val template: Template? = null,

    @SerializedName("type") val type: String? = null,
    @SerializedName("style") val style: Style? = null,
    @SerializedName("height") val height: Any? = null,
    @SerializedName("shape") val shape: String? = null,
    @SerializedName("tint") val tint: Tint? = null,
    @SerializedName("layout") val layout: Layout? = null,
    @SerializedName("image") val image: Any? = null,
    @SerializedName("aspect") val aspect: String? = null,
    @SerializedName("padding") val padding: String? = null,
    @SerializedName("vertical_offset") val verticalOffset: Int? = null,

    @SerializedName("action") val action: String? = null,
    @SerializedName("action_label") val actionLabel: String? = null,
    @SerializedName("text") val text: TextBlock? = null,
    @SerializedName("secondary_text") val secondaryText: TextBlock? = null,
    @SerializedName("margin") val margin: String? = null,

    @SerializedName("grouping") val grouping: String? = null,
    @SerializedName("selected_product") val selectedProduct: String? = null,
    @SerializedName("state") val state: String? = null,
    @SerializedName("default_style") val defaultStyle: Style? = null,
    @SerializedName("selected_style") val selectedStyle: Style? = null,
    @SerializedName("text_1") val text1: ProductText? = null,
    @SerializedName("text_2") val text2: ProductText? = null,
    @SerializedName("text_3") val text3: ProductText? = null,
    @SerializedName("text_4") val text4: ProductText? = null,

    @SerializedName("has_term_of_service") val hasTermOfService: Boolean? = null,
    @SerializedName("term_of_service") val termOfService: LinkText? = null,
    @SerializedName("has_privacy_policy") val hasPrivacyPolicy: Boolean? = null,
    @SerializedName("privacy_policy") val privacyPolicy: LinkText? = null,
    @SerializedName("has_restore_button") val hasRestoreButton: Boolean? = null,
    @SerializedName("restore_button") val restoreButton: LinkText? = null,
    @SerializedName("has_login_button") val hasLoginButton: Boolean? = null,
    @SerializedName("login_button") val loginButton: LinkText? = null,

    @SerializedName("format") val format: String? = null,
    @SerializedName("separator") val separator: String? = null,
    @SerializedName("start_text") val startText: String? = null,
    @SerializedName("before_text") val beforeText: String? = null,
    @SerializedName("after_text") val afterText: String? = null,

    @SerializedName("page_control") val pageControl: Boolean? = null,
    @SerializedName("slide_show") val slideShow: Boolean? = null,
    @SerializedName("timing") val timing: Timing? = null,
    @SerializedName("background_image") val backgroundImage: String? = null,
    @SerializedName("content_padding") val contentPadding: Int? = null,
    @SerializedName("spacing") val spacing: Int? = null
)

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal data class BotsiUiContentLayoutDto(
    @SerializedName("margin") val margin: String? = null,
    @SerializedName("spacing") val spacing: Int? = null,
    @SerializedName("layout") val layout: String? = null,
    @SerializedName("align") val align: String? = null,
    @SerializedName("padding") val padding: String? = null,
)

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal data class BotsiUiBackgroundColorDto(
    @SerializedName("background") val background: String? = null,
    @SerializedName("opacity") val opacity: Int? = null
)

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal data class BotsiUiTextFontDto(
    @SerializedName("name") val name: String? = null,
    @SerializedName("id") val id: String? = null,
    @SerializedName("types") val types: List<BotsiUiTextFontTypeDto>? = null
)

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal data class BotsiUiTextFontTypeDto(
    @SerializedName("name") val name: String? = null,
    @SerializedName("id") val id: String? = null,
    @SerializedName("isSelected") val isSelected: Boolean? = null
)



