package com.botsi.view.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.botsi.view.R
import com.botsi.view.model.content.BotsiAlign
import com.botsi.view.model.content.BotsiBadge
import com.botsi.view.model.content.BotsiButtonContent
import com.botsi.view.model.content.BotsiButtonContentLayout
import com.botsi.view.model.content.BotsiComponentStyle
import com.botsi.view.model.content.BotsiCardContent
import com.botsi.view.model.content.BotsiCardContentLayout
import com.botsi.view.model.content.BotsiCardStyle
import com.botsi.view.model.content.BotsiCarouselContent
import com.botsi.view.model.content.BotsiCarouselStyle
import com.botsi.view.model.content.BotsiColor
import com.botsi.view.model.content.BotsiColorBehaviour
import com.botsi.view.model.content.BotsiContentLayout
import com.botsi.view.model.content.BotsiFont
import com.botsi.view.model.content.BotsiFontStyleType
import com.botsi.view.model.content.BotsiFooterContent
import com.botsi.view.model.content.BotsiFooterStyle
import com.botsi.view.model.content.BotsiGradient
import com.botsi.view.model.content.BotsiHeroImageContent
import com.botsi.view.model.content.BotsiHeroImageContentStyle
import com.botsi.view.model.content.BotsiHeroImageShape
import com.botsi.view.model.content.BotsiHeroLayout
import com.botsi.view.model.content.BotsiImageAspect
import com.botsi.view.model.content.BotsiImageContent
import com.botsi.view.model.content.BotsiLayoutDirection
import com.botsi.view.model.content.BotsiLinksContent
import com.botsi.view.model.content.BotsiLinksContentLayout
import com.botsi.view.model.content.BotsiListContent
import com.botsi.view.model.content.BotsiMorePlansSheetContent
import com.botsi.view.model.content.BotsiPlansContent
import com.botsi.view.model.content.BotsiPlansControlContent
import com.botsi.view.model.content.BotsiPlansControlContentLayout
import com.botsi.view.model.content.BotsiProductContentLayout
import com.botsi.view.model.content.BotsiProductStyle
import com.botsi.view.model.content.BotsiProductTextStyle
import com.botsi.view.model.content.BotsiProductToggleContent
import com.botsi.view.model.content.BotsiProductToggleContentLayout
import com.botsi.view.model.content.BotsiProductToggleStateContent
import com.botsi.view.model.content.BotsiProductToggleStateContentLayout
import com.botsi.view.model.content.BotsiProductToggleStyle
import com.botsi.view.model.content.BotsiProductsContent
import com.botsi.view.model.content.BotsiTabControlContent
import com.botsi.view.model.content.BotsiTabControlState
import com.botsi.view.model.content.BotsiTabGroupContent
import com.botsi.view.model.content.BotsiTextContent
import com.botsi.view.model.content.BotsiTimerContent

private val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

@Composable
internal fun BotsiHeroImageContent?.toImageHeightPx(): Float {
    val configuration = LocalWindowInfo.current
    val screenHeightPx = configuration.containerSize.height

    return remember(this) { screenHeightPx * ((this?.height ?: 0f) / 100f) }
}

@Composable
internal fun BotsiHeroImageContent?.toImageHeightDp(): Dp {
    val density = LocalDensity.current
    return with(density) { toImageHeightPx().toDp() }
}

internal fun String?.toColor(opacity: Float? = null): Color {
    return runCatching {
        this?.let { Color(it.toColorInt()).copy(alpha = (opacity ?: 100f) / 100f) }
            ?: Color.Unspecified
    }.getOrDefault(Color.Unspecified)
}

internal fun BotsiColorBehaviour?.toBrush(opacity: Float? = null): Brush {
    return when (this) {
        is BotsiColor -> {
            val color = this.color.toColor(opacity)
            Brush.linearGradient(colors = listOf(color, color))
        }

        is BotsiGradient -> {
            if (colors.isNullOrEmpty()) {
                return Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
            }
            val degrees = this.degrees ?: 0f

            BotsiLinearGradient(
                colors = colors.map { it.color.toColor() },
                stops = colors.map { (it.position ?: 0f) / 100f },
                angleInDegrees = degrees,
            )
        }

        null -> Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
    }
}

@Composable
internal fun BotsiComponentStyle?.toBorder(): Modifier {
    return this?.let { style ->
        if (style.borderThickness == null || style.borderThickness == 0f) {
            Modifier
        } else {
            Modifier.border(
                width = style.borderThickness.dp,
                color = style.borderColor.toColor(style.borderOpacity),
                shape = style.toShape()
            )
        }
    } ?: Modifier
}

@Composable
internal fun BotsiFooterStyle?.toBorder(): Modifier {
    return this?.let { style ->
        if (style.borderThickness == null || style.borderThickness == 0) {
            Modifier
        } else {
            Modifier.border(
                width = style.borderThickness.dp,
                color = style.borderColor.toColor(style.borderOpacity),
                shape = style.toShape()
            )
        }
    } ?: Modifier
}

@Composable
internal fun BotsiCardStyle?.toBorder(): Modifier {
    return this?.let { style ->
        if (style.borderThickness == null || style.borderThickness == 0) {
            Modifier
        } else {
            Modifier.border(
                width = style.borderThickness.dp,
                color = style.borderColor.toColor(style.borderOpacity),
                shape = style.toShape()
            )
        }
    } ?: Modifier
}

internal fun BotsiComponentStyle?.toBorderStroke(): BorderStroke {
    return this?.let { style ->
        if (style.borderThickness == null || style.borderThickness == 0f) {
            BorderStroke(0.dp, Color.Transparent)
        } else {
            BorderStroke(
                width = style.borderThickness.dp,
                color = style.borderColor.toColor(style.borderOpacity),
            )
        }
    } ?: BorderStroke(0.dp, Color.Transparent)
}

@Composable
internal fun BotsiComponentStyle?.toBackground(): Modifier {
    return this?.let { style ->
        Modifier.background(
            color = style.color.toColor(style.opacity),
            shape = style.toShape()
        )
    } ?: Modifier
}

@Composable
internal fun BotsiComponentStyle?.toBackgroundFillColor(): Modifier {
    return this?.let { style ->
        Modifier.background(
            brush = style.fillColor.toBrush(style.opacity),
            shape = style.toShape()
        )
    } ?: Modifier
}

@Composable
internal fun BotsiCardStyle?.toBackground(): Modifier {
    return this?.let { style ->
        Modifier.background(
            brush = style.color.toBrush(style.opacity),
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

internal fun BotsiAlign?.toAlignmentHorizontal(): Alignment.Horizontal {
    return when (this) {
        BotsiAlign.Left -> Alignment.Start
        BotsiAlign.Right -> Alignment.End
        BotsiAlign.Center -> Alignment.CenterHorizontally
        else -> Alignment.Start
    }
}

internal fun BotsiAlign?.toAlignmentVertical(): Alignment.Vertical {
    return when (this) {
        BotsiAlign.Top -> Alignment.Top
        BotsiAlign.Bottom -> Alignment.Bottom
        BotsiAlign.Center -> Alignment.CenterVertically
        else -> Alignment.CenterVertically
    }
}

internal fun BotsiHeroImageContent?.toShape(offsetValue: Float = 0f): Shape {
    return this?.shape?.let {
        if (this.style == BotsiHeroImageContentStyle.Overlay && offsetValue <= 0f) {
            return RectangleShape
        }
        when (it) {
            BotsiHeroImageShape.Circle -> GenericShape { size, _ ->
                val radius = minOf(size.width, size.height) / 2f
                val centerX = size.width / 2f
                val centerY = size.height / 2f
                addOval(
                    Rect(
                        left = centerX - radius,
                        top = centerY - radius,
                        right = centerX + radius,
                        bottom = centerY + radius
                    )
                )
            }

            BotsiHeroImageShape.Leaf -> RoundedCornerShape(
                topStart = 150.dp,
                bottomEnd = 150.dp,
            )

            BotsiHeroImageShape.RoundedRectangle -> {
                if (this.style == BotsiHeroImageContentStyle.Overlay) {
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                    )
                } else {
                    RoundedCornerShape(24.dp)
                }
            }

            BotsiHeroImageShape.ConcaveMask -> GenericShape { size, _ ->
                val ovalDepth = size.height * 0.02f
                moveTo(0f, 0f)
                cubicTo(
                    size.width * 0.25f, ovalDepth,
                    size.width * 0.75f, ovalDepth,
                    size.width, 0f
                )
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }

            BotsiHeroImageShape.ConvexMask -> GenericShape { size, _ ->
                val ovalHeight = size.height * 0.02f
                val ovalWidth = size.width
                val ovalLeft = (size.width - ovalWidth) / 2f
                val ovalRight = ovalLeft + ovalWidth
                moveTo(0f, ovalHeight)
                lineTo(ovalLeft, ovalHeight)
                cubicTo(
                    ovalLeft, ovalHeight * 0.5f,
                    ovalRight, ovalHeight * 0.5f,
                    ovalRight, ovalHeight
                )
                lineTo(size.width, ovalHeight)
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }

            else -> RectangleShape
        }
    } ?: RectangleShape
}

internal fun BotsiComponentStyle?.toShape(): Shape {
    return this?.radius.toShape()
}

internal fun BotsiFooterStyle?.toShape(): Shape {
    return this?.radius.toShape()
}

internal fun BotsiCardStyle?.toShape(): Shape {
    return this?.radius.toShape()
}

@Composable
internal fun BotsiProductStyle?.toBackground(): Modifier {
    return this?.let { style ->
        Modifier.background(
            brush = style.color.toBrush(style.opacity),
            shape = style.radius.toShape()
        )
    } ?: Modifier
}

@Composable
internal fun BotsiBadge?.toBackground(): Modifier {
    return this?.let { style ->
        Modifier.background(
            brush = style.badgeColor.toBrush(style.badgeOpacity),
            shape = style.badgeRadius.toShape()
        )
    } ?: Modifier
}

@Composable
internal fun BotsiProductStyle?.toBorder(): Modifier {
    return this?.let { style ->
        if (style.borderThickness == null || style.borderThickness == 0) {
            Modifier
        } else {
            Modifier.border(
                width = style.borderThickness.dp,
                color = style.borderColor.toColor(style.borderOpacity),
                shape = style.radius.toShape()
            )
        }
    } ?: Modifier
}

internal fun BotsiContentLayout?.toPaddings(extraTopPadding: Dp = Dp.Hairline): PaddingValues {
    return this?.margin.toPaddings(extraTopPadding)
}

internal fun BotsiFooterContent?.toPaddings(): PaddingValues {
    return this?.padding.toPaddings()
}

internal fun BotsiTextContent?.toPaddings(): PaddingValues {
    return this?.margin.toPaddings()
}

internal fun BotsiImageContent?.toPaddings(): PaddingValues {
    return this?.padding.toPaddings()
}

internal fun BotsiButtonContent?.toPaddings(): PaddingValues {
    return this?.margin.toPaddings()
}

internal fun BotsiButtonContentLayout?.toPaddings(): PaddingValues {
    return this?.padding.toPaddings()
}

internal fun BotsiListContent?.toPaddings(): PaddingValues {
    return this?.padding.toPaddings()
}

internal fun BotsiCardContent?.toPaddings(): PaddingValues {
    return this?.margin.toPaddings()
}

internal fun BotsiCardContentLayout?.toPaddings(): PaddingValues {
    return this?.padding.toPaddings()
}

internal fun BotsiLinksContent?.toPaddings(): PaddingValues {
    return this?.padding.toPaddings()
}

internal fun BotsiCarouselContent?.toPaddings(): PaddingValues {
    return this?.padding.toPaddings()
}

internal fun BotsiCarouselContent?.toContentPaddings(): PaddingValues {
    return this?.contentPadding.toPaddings()
}

internal fun BotsiCarouselStyle?.toPaddings(): PaddingValues {
    return this?.padding.toPaddings()
}

internal fun BotsiTimerContent?.toPaddings(): PaddingValues {
    return this?.padding.toPaddings()
}


internal fun BotsiCarouselStyle?.toArrangementHorizontal(alignment: Alignment.Horizontal): Arrangement.Horizontal {
    return this?.spacing.toArrangementHorizontal(alignment)
}

internal fun BotsiContentLayout?.toArrangementVertical(): Arrangement.Vertical {
    return this?.spacing.toArrangementVertical()
}

internal fun BotsiFooterContent?.toArrangementVertical(): Arrangement.Vertical {
    return this?.spacing.toArrangementVertical()
}

internal fun BotsiListContent?.toArrangementVertical(): Arrangement.Vertical {
    return this?.itemSpacing.toArrangementVertical()
}

internal fun BotsiLinksContentLayout?.toArrangementVertical(alignment: Alignment.Vertical = Alignment.Top): Arrangement.Vertical {
    return ((this?.spacing ?: 4) + 4).toArrangementVertical(alignment)
}

internal fun BotsiLinksContentLayout?.toArrangementHorizontal(alignment: Alignment.Horizontal = Alignment.Start): Arrangement.Horizontal {
    return ((this?.spacing ?: 4) + 4).toArrangementHorizontal(alignment)
}

internal fun BotsiCardContentLayout?.toAlignmentHorizontal(): Alignment.Horizontal {
    return this?.align.toAlignmentHorizontal()
}

internal fun Float?.toFontSize(): TextUnit {
    return ((this ?: 14f) * 1.2f).sp
}

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

internal fun BotsiHeroLayout?.toPaddings(): PaddingValues {
    return this?.padding.toPaddings()
}

internal fun BotsiProductsContent?.toPaddings(): PaddingValues {
    return this?.padding.toPaddings()
}

internal fun BotsiProductContentLayout?.toPaddings(): PaddingValues {
    return this?.padding.toPaddings()
}

internal fun BotsiProductToggleContent?.toPaddings(): PaddingValues {
    return this?.padding.toPaddings()
}

internal fun BotsiProductToggleContentLayout?.toPaddings(): PaddingValues {
    return this?.padding.toPaddings()
}

internal fun BotsiProductToggleStateContent?.toPaddings(): PaddingValues {
    return this?.padding.toPaddings()
}

internal fun BotsiProductToggleStateContentLayout?.toPaddings(): PaddingValues {
    return this?.padding.toPaddings()
}

internal fun BotsiTabControlContent?.toPaddings(): PaddingValues {
    return this?.padding.toPaddings()
}

internal fun BotsiTabControlState?.toPaddings(): PaddingValues {
    return this?.padding.toPaddings()
}

internal fun BotsiTabGroupContent?.toPaddings(): PaddingValues {
    return this?.padding.toPaddings()
}

internal fun BotsiPlansContent?.toPaddings(): PaddingValues {
    return this?.padding.toPaddings()
}

internal fun BotsiPlansControlContent?.toPaddings(): PaddingValues {
    return this?.margin.toPaddings()
}

internal fun BotsiPlansControlContentLayout?.toPaddings(): PaddingValues {
    return this?.padding.toPaddings()
}

internal fun BotsiMorePlansSheetContent?.toPaddings(): PaddingValues {
    return this?.padding.toPaddings()
}

@Composable
internal fun BotsiProductToggleStyle?.toBackground(): Modifier {
    return this?.let { style ->
        Modifier.background(
            brush = style.color.toBrush(style.opacity),
            shape = style.radius.toShape()
        )
    } ?: Modifier
}

@Composable
internal fun BotsiProductToggleStyle?.toBorder(): Modifier {
    return this?.let { style ->
        if (style.borderThickness == null || style.borderThickness == 0) {
            Modifier
        } else {
            Modifier.border(
                width = style.borderThickness.dp,
                color = style.borderColor.toColor(style.borderOpacity),
                shape = style.radius.toShape()
            )
        }

    } ?: Modifier
}

internal fun BotsiProductContentLayout?.toArrangementVertical(): Arrangement.Vertical {
    return this?.spacing.toArrangementVertical()
}

internal fun BotsiProductContentLayout?.toArrangementHorizontal(): Arrangement.Horizontal {
    return this?.spacing.toArrangementHorizontal()
}

internal fun BotsiProductToggleContentLayout?.toArrangementVertical(
    alignment: Alignment.Vertical = Alignment.Top
): Arrangement.Vertical {
    return this?.spacing.toArrangementVertical(alignment)
}

internal fun BotsiProductToggleContentLayout?.toArrangementHorizontal(
    alignment: Alignment.Horizontal = Alignment.Start
): Arrangement.Horizontal {
    return this?.spacing.toArrangementHorizontal(alignment)
}

internal fun BotsiProductToggleStateContentLayout?.toArrangementVertical(): Arrangement.Vertical {
    return this?.spacing.toArrangementVertical()
}

internal fun BotsiProductToggleStateContentLayout?.toArrangementHorizontal(): Arrangement.Horizontal {
    return this?.spacing.toArrangementHorizontal()
}

internal fun BotsiProductContentLayout?.toAlignmentHorizontal(): Alignment.Horizontal {
    return this?.align.toAlignmentHorizontal()
}

internal fun BotsiProductContentLayout?.toAlignmentVertical(): Alignment.Vertical {
    return this?.align.toAlignmentVertical()
}

internal fun BotsiProductToggleStateContentLayout?.toAlignmentHorizontal(): Alignment.Horizontal {
    return this?.align.toAlignmentHorizontal()
}

internal fun BotsiProductToggleStateContentLayout?.toAlignmentVertical(): Alignment.Vertical {
    return this?.align.toAlignmentVertical()
}

internal fun BotsiLayoutDirection?.toArrangementHorizontal(spacing: Int? = null): Arrangement.Horizontal {
    return when (this) {
        BotsiLayoutDirection.Horizontal -> Arrangement.spacedBy((spacing ?: 0).dp, Alignment.Start)
        BotsiLayoutDirection.Vertical -> Arrangement.Start
        else -> Arrangement.Start
    }
}

@Composable
internal fun BotsiProductTextStyle?.toTextStyle(): TextStyle {
    return this?.let { textStyle ->
        textStyle.font.toTextStyle().copy(
            fontSize = (textStyle.size ?: 14).sp,
            color = textStyle.color.toColor(textStyle.opacity)
        )
    } ?: TextStyle()
}

@Composable
internal fun BotsiProductTextStyle?.toSelectedTextStyle(): TextStyle {
    return this?.let { textStyle ->
        textStyle.font.toTextStyle().copy(
            fontSize = (textStyle.size ?: 14).sp,
            color = textStyle.selectedColor.toColor(textStyle.selectedOpacity)
        )
    } ?: TextStyle()
}

private fun List<Int>?.toPaddings(extraTopPadding: Dp = Dp.Hairline): PaddingValues = this?.let {
    if (it.size == 1) {
        PaddingValues(
            top = it.getOrElse(0) { 0 }.dp + extraTopPadding,
            bottom = it.getOrElse(0) { 0 }.dp,
            start = it.getOrElse(0) { 0 }.dp,
            end = it.getOrElse(0) { 0 }.dp,
        )
    } else {
        PaddingValues(
            top = it.getOrElse(0) { 0 }.dp + extraTopPadding,
            end = it.getOrElse(1) { 0 }.dp,
            bottom = it.getOrElse(2) { 0 }.dp,
            start = it.getOrElse(3) { 0 }.dp,
        )
    }
} ?: PaddingValues()

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

private fun Int?.toArrangementVertical(alignment: Alignment.Vertical = Alignment.Top): Arrangement.Vertical =
    Arrangement.spacedBy((this ?: 0).dp, alignment)

private fun Int?.toArrangementHorizontal(alignment: Alignment.Horizontal = Alignment.Start): Arrangement.Horizontal =
    Arrangement.spacedBy((this ?: 0).dp, alignment)
