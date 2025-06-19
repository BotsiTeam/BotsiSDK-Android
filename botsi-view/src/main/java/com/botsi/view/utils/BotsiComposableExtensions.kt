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
import com.botsi.view.model.content.BotsiHeroImageContent
import com.botsi.view.model.content.BotsiHeroImageContentStyle
import com.botsi.view.model.content.BotsiHeroImageShape
import com.botsi.view.model.content.BotsiHeroLayout
import com.botsi.view.model.content.BotsiImageAspect
import com.botsi.view.model.content.BotsiImageContent
import com.botsi.view.model.content.BotsiLinksContent
import com.botsi.view.model.content.BotsiLinksContentLayout
import com.botsi.view.model.content.BotsiListContent
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

internal fun BotsiBackgroundColor?.toColor(): Color {
    return this?.background?.toColor(opacity) ?: Color.Unspecified
}

internal fun String?.toColor(opacity: Float? = null): Color {
    return runCatching {
        this?.let { Color(it.toColorInt()).copy(alpha = (opacity ?: 100f) / 100f) } ?: Color.Unspecified
    }.getOrDefault(Color.Unspecified)
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

internal fun BotsiButtonStyle?.toShape(): Shape {
    return this?.radius.toShape()
}

internal fun BotsiFooterStyle?.toShape(): Shape {
    return this?.radius.toShape()
}

internal fun BotsiCardStyle?.toShape(): Shape {
    return this?.radius.toShape()
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

internal fun BotsiHeroLayout?.toPaddings(): PaddingValues {
    return this?.padding.toPaddings()
}

internal fun BotsiCarouselStyle?.toArrangement(alignment: Alignment.Horizontal): Arrangement.Horizontal {
    return this?.spacing.toArrangementHorizontal(alignment)
}

internal fun BotsiContentLayout?.toArrangement(): Arrangement.Vertical {
    return this?.spacing.toArrangement()
}

internal fun BotsiFooterContent?.toArrangement(): Arrangement.Vertical {
    return this?.spacing.toArrangement()
}

internal fun BotsiListContent?.toArrangement(): Arrangement.Vertical {
    return this?.itemSpacing.toArrangement()
}

internal fun BotsiLinksContentLayout?.toArrangement(alignment: Alignment.Vertical = Alignment.Top): Arrangement.Vertical {
    return ((this?.spacing ?: 4) + 4).toArrangement(alignment)
}

internal fun BotsiLinksContentLayout?.toArrangementHorizontal(alignment: Alignment.Horizontal = Alignment.Start): Arrangement.Horizontal {
    return ((this?.spacing ?: 4) + 4).toArrangementHorizontal(alignment)
}

internal fun BotsiCardContentLayout?.toAlignment(): Alignment.Horizontal {
    return this?.align.toAlignmentHorizontal()
}

internal fun Float?.toFontSize(): TextUnit {
    return ((this?.toFloat() ?: 14f) * 1.2f).sp
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

private fun Int?.toArrangement(alignment: Alignment.Vertical = Alignment.Top): Arrangement.Vertical =
    Arrangement.spacedBy((this ?: 0).dp, alignment)

private fun Int?.toArrangementHorizontal(alignment: Alignment.Horizontal = Alignment.Start): Arrangement.Horizontal =
    Arrangement.spacedBy((this ?: 0).dp, alignment)
