package com.perfomer.checkielite.common.ui.presentation.transition

import androidx.compose.animation.EnterExitState
import androidx.compose.animation.core.animateDp
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberConstraintsSizeResolver
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.size.SizeResolver
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import com.perfomer.checkielite.core.navigation.transition.LocalNavigationAnimatedVisibilityScope
import com.perfomer.checkielite.core.navigation.transition.LocalSharedNavigationImageScope
import com.perfomer.checkielite.core.navigation.transition.sharedNavigationImage
import com.perfomer.checkielite.core.navigation.transition.LocalSharedNavigationContent
import com.perfomer.checkielite.core.navigation.transition.LocalSharedTransitionScope
import com.perfomer.checkielite.core.navigation.transition.rememberSharedContentConfig
import com.perfomer.checkielite.core.navigation.transition.sharedNavigationTween

@Immutable
private data class SharedImageKey(val contentId: Any, val imageUri: String)

/**
 * Both endpoints use the same crop, cache entry and clipping order. The size resolver lives
 * outside the animated bounds so Coil does not restart decoding at every animation frame.
 */
@Composable
fun SharedImage(
    imageUri: String,
    cornerRadius: Dp,
    overlayCornerRadius: Dp,
    onState: (AsyncImagePainter.State) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val sharedScope = LocalSharedTransitionScope.current
    val visibilityScope = LocalNavigationAnimatedVisibilityScope.current
    val sharedContent = LocalSharedNavigationContent.current
    val context = LocalPlatformContext.current
    val sizeResolver = rememberConstraintsSizeResolver()
    // AsyncImage updates a ConstraintsSizeResolver from its own animated measurement. Hide the
    // concrete resolver type so only our outer, stable placeholder constraints drive decoding.
    val requestSizeResolver = remember(sizeResolver) { SizeResolver { sizeResolver.size() } }
    val request = remember(context, imageUri, requestSizeResolver) {
        ImageRequest.Builder(context)
            .data(imageUri)
            .size(requestSizeResolver)
            .memoryCacheKey(imageUri)
            .placeholderMemoryCacheKey(imageUri)
            .crossfade(false)
            .build()
    }

    val imageModifier = if (LocalSharedNavigationImageScope.current != null) {
        Modifier
            .then(sizeResolver)
            .sharedNavigationImage(imageUri) { sharedContent?.isEnabled?.invoke() != false }
            .then(modifier)
            .clip(RoundedCornerShape(cornerRadius))
    } else if (sharedScope != null && visibilityScope != null && sharedContent != null) {
        val config = rememberSharedContentConfig(sharedContent)
        val radius by visibilityScope.transition.animateDp(
            transitionSpec = {
                sharedNavigationTween()
            },
            label = "Shared image corners",
        ) { state ->
            if (state == EnterExitState.Visible) cornerRadius else overlayCornerRadius
        }

        with(sharedScope) {
            val sharedContentState = rememberSharedContentState(
                key = SharedImageKey(sharedContent.id, imageUri),
                config = config,
            )
            Modifier
                .then(sizeResolver)
                .sharedElement(
                    sharedContentState = sharedContentState,
                    animatedVisibilityScope = visibilityScope,
                    boundsTransform = { _, _ -> sharedNavigationTween() },
                    zIndexInOverlay = 1F,
                )
                .then(modifier)
                .clip(RoundedCornerShape(if (sharedContentState.isMatchFound) radius else cornerRadius))
        }
    } else {
        Modifier
            .then(sizeResolver)
            .then(modifier)
            .clip(RoundedCornerShape(cornerRadius))
    }

    AsyncImage(
        model = request,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        onState = onState,
        modifier = imageModifier.background(LocalCuiPalette.current.BackgroundSecondary)
    )
}
