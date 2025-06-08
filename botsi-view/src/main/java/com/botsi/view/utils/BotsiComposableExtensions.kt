package com.botsi.view.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.botsi.view.R
import com.botsi.view.model.content.BotsiAlign
import com.botsi.view.model.content.BotsiBackgroundColor
import com.botsi.view.model.content.BotsiButtonContent
import com.botsi.view.model.content.BotsiButtonContentLayout
import com.botsi.view.model.content.BotsiButtonStyle
import com.botsi.view.model.content.BotsiCardContent
import com.botsi.view.model.content.BotsiCardContentLayout
import com.botsi.view.model.content.BotsiCardStyle
import com.botsi.view.model.content.BotsiCarouselContent
import com.botsi.view.model.content.BotsiCarouselStyle
import com.botsi.view.model.content.BotsiContentLayout
import com.botsi.view.model.content.BotsiFont
import com.botsi.view.model.content.BotsiFontStyleType
import com.botsi.view.model.content.BotsiFooterContent
import com.botsi.view.model.content.BotsiFooterStyle
import com.botsi.view.model.content.BotsiImageAspect
import com.botsi.view.model.content.BotsiImageContent
import com.botsi.view.model.content.BotsiLinksContent
import com.botsi.view.model.content.BotsiLinksContentLayout
import com.botsi.view.model.content.BotsiListContent
import com.botsi.view.model.content.BotsiTextContent

private val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

@Composable
internal fun BotsiBackgroundColor?.toColor(): Color {
    return this?.background?.let { Color(it.toColorInt()).copy(alpha = (opacity ?: 100f) / 100f) } ?: Color.Unspecified
}

@Composable
internal fun String?.toColor(opacity: Float? = null): Color {
    return this?.let { Color(it.toColorInt()).copy(alpha = (opacity ?: 100f) / 100f) } ?: Color.Unspecified
}

@Composable
internal fun BotsiButtonStyle?.toBorder(): Modifier {
    return this?.let { style ->
        Modifier.border(
            width = (style.borderThickness ?: 0f).dp,
            color = style.borderColor.toColor(style.borderOpacity),
            shape = style.toShape()
        )
    } ?: Modifier
}

@Composable
internal fun BotsiFooterStyle?.toBorder(): Modifier {
    return this?.let { style ->
        Modifier.border(
            width = (style.borderThickness ?: 0).dp,
            color = style.borderColor.toColor(style.borderOpacity),
            shape = style.toShape()
        )
    } ?: Modifier
}

@Composable
internal fun BotsiCardStyle?.toBorder(): Modifier {
    return this?.let { style ->
        Modifier.border(
            width = (style.borderThickness ?: 0).dp,
            color = style.borderColor.toColor(style.borderOpacity),
            shape = style.toShape()
        )
    } ?: Modifier
}

@Composable
internal fun BotsiButtonStyle?.toBorderStroke(): BorderStroke? {
    return this?.let { style ->
        BorderStroke(
            width = (style.borderThickness ?: 0f).dp,
            color = style.borderColor.toColor(style.borderOpacity),
        )
    }
}

@Composable
internal fun BotsiButtonStyle?.toBackground(): Modifier {
    return this?.let { style ->
        Modifier.background(
            color = style.color.toColor(style.opacity),
            shape = style.toShape()
        )
    } ?: Modifier
}

@Composable
internal fun BotsiCardStyle?.toBackground(): Modifier {
    return this?.let { style ->
        Modifier.background(
            color = style.color.toColor(style.opacity),
            shape = style.toShape()
        )
    } ?: Modifier
}

@Composable
internal fun BotsiFooterStyle?.toBackground(): Modifier {
    return this?.let { style ->
        Modifier.background(
            color = style.color.toColor(style.opacity),
            shape = style.toShape()
        )
    } ?: Modifier
}

@Composable
internal fun BotsiAlign?.toAlignment(): Alignment {
    return when (this) {
        BotsiAlign.Left -> Alignment.CenterStart
        BotsiAlign.Right -> Alignment.CenterEnd
        BotsiAlign.Center -> Alignment.Center
        BotsiAlign.Bottom -> Alignment.BottomCenter
        BotsiAlign.Top -> Alignment.TopCenter
        else -> Alignment.TopStart
    }
}

@Composable
internal fun BotsiAlign?.toAlignmentHorizontal(): Alignment.Horizontal {
    return when (this) {
        BotsiAlign.Left -> Alignment.Start
        BotsiAlign.Right -> Alignment.End
        BotsiAlign.Center -> Alignment.CenterHorizontally
        else -> Alignment.Start
    }
}

@Composable
internal fun BotsiButtonStyle?.toShape(): Shape {
    return this?.radius.toShape()
}

@Composable
internal fun BotsiFooterStyle?.toShape(): Shape {
    return this?.radius.toShape()
}

@Composable
internal fun BotsiCardStyle?.toShape(): Shape {
    return this?.radius.toShape()
}

@Composable
internal fun BotsiContentLayout?.toPaddings(): PaddingValues {
    return this?.margin.toPaddings()
}

@Composable
internal fun BotsiFooterContent?.toPaddings(): PaddingValues {
    return this?.padding.toPaddings()
}

@Composable
internal fun BotsiTextContent?.toPaddings(): PaddingValues {
    return this?.margin.toPaddings()
}

@Composable
internal fun BotsiImageContent?.toPaddings(): PaddingValues {
    return this?.padding.toPaddings()
}

@Composable
internal fun BotsiButtonContent?.toPaddings(): PaddingValues {
    return this?.margin.toPaddings()
}

@Composable
internal fun BotsiButtonContentLayout?.toPaddings(): PaddingValues {
    return this?.padding.toPaddings()
}

@Composable
internal fun BotsiListContent?.toPaddings(): PaddingValues {
    return this?.padding.toPaddings()
}

@Composable
internal fun BotsiCardContent?.toPaddings(): PaddingValues {
    return this?.margin.toPaddings()
}

@Composable
internal fun BotsiCardContentLayout?.toPaddings(): PaddingValues {
    return this?.padding.toPaddings()
}

@Composable
internal fun BotsiLinksContent?.toPaddings(): PaddingValues {
    return this?.padding.toPaddings()
}

@Composable
internal fun BotsiCarouselContent?.toPaddings(): PaddingValues {
    return this?.padding.toPaddings()
}

@Composable
internal fun BotsiCarouselContent?.toContentPaddings(): PaddingValues {
    return this?.contentPadding.toPaddings()
}

@Composable
internal fun BotsiCarouselStyle?.toPaddings(): PaddingValues {
    return this?.padding.toPaddings()
}

@Composable
internal fun BotsiCarouselContent?.toArrangement(alignment: Alignment.Vertical): Arrangement.Vertical {
    return this?.spacing.toArrangement(alignment)
}

@Composable
internal fun BotsiCarouselStyle?.toArrangement(alignment: Alignment.Horizontal): Arrangement.Horizontal {
    return this?.spacing.toArrangementHorizontal(alignment)
}

@Composable
internal fun BotsiContentLayout?.toArrangement(): Arrangement.Vertical {
    return this?.spacing.toArrangement()
}

@Composable
internal fun BotsiFooterContent?.toArrangement(): Arrangement.Vertical {
    return this?.spacing.toArrangement()
}

@Composable
internal fun BotsiListContent?.toArrangement(): Arrangement.Vertical {
    return this?.itemSpacing.toArrangement()
}

@Composable
internal fun BotsiLinksContentLayout?.toArrangement(): Arrangement.Vertical {
    return ((this?.spacing ?: 4) + 4).toArrangement()
}

@Composable
internal fun BotsiLinksContentLayout?.toArrangementHorizontal(): Arrangement.Horizontal {
    return ((this?.spacing ?: 4) + 4).toArrangementHorizontal()
}

@Composable
internal fun BotsiCardContentLayout?.toAlignment(): Alignment.Horizontal {
    return this?.align.toAlignmentHorizontal()
}

internal fun Float?.toFontSize(): TextUnit {
    return ((this?.toFloat() ?: 14f) * 1.2f).sp
}

@Composable
internal fun BotsiFont?.toTextStyle(): TextStyle {
    return this?.let {
        val selectedType = types?.find { it.isSelected == true }
        val fontName = it.name.orEmpty().run {
            replace(selectedType?.name.orEmpty(), "").trim()
        }
        val font = GoogleFont(name = fontName.takeIf { !it.contains("System") } ?: "Roboto")
        val fontFamily = FontFamily(
            Font(
                googleFont = font,
                fontProvider = provider,
                weight =
                    if (selectedType?.fontStyle == BotsiFontStyleType.Bold) {
                        FontWeight.W700
                    } else {
                        when (selectedType?.fontWeight) {
                            FontWeight.W100.weight -> FontWeight.W100
                            FontWeight.W200.weight -> FontWeight.W200
                            FontWeight.W300.weight -> FontWeight.W300
                            FontWeight.W400.weight -> FontWeight.W400
                            FontWeight.W500.weight -> FontWeight.W500
                            FontWeight.W600.weight -> FontWeight.W600
                            FontWeight.W700.weight -> FontWeight.W700
                            FontWeight.W800.weight -> FontWeight.W800
                            FontWeight.W900.weight -> FontWeight.W900
                            else -> FontWeight.Normal
                        }
                    },
                style = when (selectedType?.fontStyle) {
                    BotsiFontStyleType.Normal -> FontStyle.Normal
                    BotsiFontStyleType.Italic -> FontStyle.Italic
                    else -> FontStyle.Normal
                }
            )
        )
        TextStyle(fontFamily = fontFamily)
    } ?: TextStyle()
}

@Composable
internal fun BotsiImageContent?.toContentScale(): ContentScale {
    return this?.let {
        when (it.aspect) {
            BotsiImageAspect.Fit -> ContentScale.Fit
            BotsiImageAspect.Fill -> ContentScale.Crop
            BotsiImageAspect.Stretch -> ContentScale.FillBounds
            else -> ContentScale.Fit
        }
    } ?: ContentScale.Fit
}

@Composable
private fun List<Int>?.toPaddings(): PaddingValues = this?.let {
    if (it.size == 1) {
        PaddingValues(it.getOrElse(0) { 0 }.dp)
    } else {
        PaddingValues(
            top = it.getOrElse(0) { 0 }.dp,
            end = it.getOrElse(1) { 0 }.dp,
            bottom = it.getOrElse(2) { 0 }.dp,
            start = it.getOrElse(3) { 0 }.dp,
        )
    }
} ?: PaddingValues()

@Composable
private fun List<Int>?.toShape(): Shape = this?.let {
    if (it.size == 1) {
        RoundedCornerShape((it.getOrNull(0) ?: 0).dp)
    } else {
        RoundedCornerShape(
            topStart = (it.getOrNull(0) ?: 0).dp,
            topEnd = (it.getOrNull(1) ?: 0).dp,
            bottomEnd = (it.getOrNull(2) ?: 0).dp,
            bottomStart = (it.getOrNull(3) ?: 0).dp,
        )
    }
} ?: RoundedCornerShape(0.dp)

@Composable
private fun Int?.toArrangement(alignment: Alignment.Vertical = Alignment.Top): Arrangement.Vertical =
    Arrangement.spacedBy((this ?: 0).dp, alignment)

@Composable
private fun Int?.toArrangementHorizontal(alignment: Alignment.Horizontal = Alignment.Start): Arrangement.Horizontal =
    Arrangement.spacedBy((this ?: 0).dp, alignment)
