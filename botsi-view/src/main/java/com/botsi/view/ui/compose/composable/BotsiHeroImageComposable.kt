package com.botsi.view.ui.compose.composable

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil3.compose.AsyncImage
import com.botsi.view.model.content.BotsiHeroImageContent
import com.botsi.view.utils.toBrush
import com.botsi.view.utils.toImageHeightDp
import com.botsi.view.utils.toPaddings
import com.botsi.view.utils.toShape
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView

@UnstableApi
@Composable
private fun BotsiVideoView(
    modifier: Modifier = Modifier,
    videoUrl: String,
    contentScale: ContentScale = ContentScale.Crop
) {
    val context = LocalContext.current

    val exoPlayer = remember(videoUrl) {
        ExoPlayer.Builder(context).build().apply {
            repeatMode = Player.REPEAT_MODE_ONE
            playWhenReady = true
        }
    }

    DisposableEffect(exoPlayer, videoUrl) {
        val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()

        onDispose {
            exoPlayer.stop()
            exoPlayer.release()
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
                useController = false
                resizeMode = when (contentScale) {
                    ContentScale.Crop -> AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                    else -> AspectRatioFrameLayout.RESIZE_MODE_FIT
                }
            }
        },
        update = { playerView ->
            playerView.player = exoPlayer
        }
    )
}

@UnstableApi
@Composable
internal fun BotsiHeroImageOverlayComposable(
    modifier: Modifier = Modifier,
    content: BotsiHeroImageContent,
) {
    val backgroundUrl = remember(content) { content.backgroundImage }
    val isVideo = remember(content) { content.type == "video" }

    if (isVideo && !backgroundUrl.isNullOrEmpty()) {
        BotsiVideoView(
            modifier = modifier.height(content.toImageHeightDp()),
            videoUrl = backgroundUrl,
            contentScale = ContentScale.Crop
        )
    } else {
        AsyncImage(
            modifier = modifier.height(content.toImageHeightDp()),
            model = backgroundUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
    }
}

@UnstableApi
@Composable
internal fun BotsiHeroImageTransparentComposable(
    modifier: Modifier = Modifier,
    content: BotsiHeroImageContent,
) {
    val backgroundUrl = remember(content) { content.backgroundImage }
    val isVideo = remember(content) { content.type == "video" }
    val brush = remember(content.tint) {
        content.tint?.fillColor.toBrush()
    }
    val alpha = remember(content.tint) {
        (content.tint?.opacity ?: 100f) / 100f
    }

    if (isVideo && !backgroundUrl.isNullOrEmpty()) {
        BotsiVideoView(
            modifier = modifier
                .fillMaxSize()
                .drawWithContent {
                    drawContent()
                    drawRect(brush = brush, alpha = alpha)
                },
            videoUrl = backgroundUrl,
            contentScale = ContentScale.Crop
        )
    } else {
        AsyncImage(
            modifier = modifier
                .fillMaxSize()
                .drawWithContent {
                    drawContent()
                    drawRect(brush = brush, alpha = alpha)
                },
            model = backgroundUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
    }
}

@UnstableApi
@Composable
internal fun BotsiHeroImageFlatComposable(
    modifier: Modifier = Modifier,
    content: BotsiHeroImageContent,
) {
    val backgroundUrl = remember(content) { content.backgroundImage }
    val isVideo = remember(content) { content.type == "video" }
    val shape = remember(content) { content.toShape() }
    val paddings = remember(content) { content.layout.toPaddings() }
    val verticalOffset = remember(content) { (content.layout?.verticalOffset ?: 0).dp }

    if (isVideo && !backgroundUrl.isNullOrEmpty()) {
        BotsiVideoView(
            modifier = modifier
                .padding(paddings)
                .fillMaxWidth()
                .height(content.toImageHeightDp())
                .clip(shape)
                .offset(y = verticalOffset),
            videoUrl = backgroundUrl,
            contentScale = ContentScale.Crop
        )
    } else {
        AsyncImage(
            modifier = modifier
                .padding(paddings)
                .fillMaxWidth()
                .height(content.toImageHeightDp())
                .clip(shape)
                .offset(y = verticalOffset),
            model = backgroundUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}
