@file:SuppressLint("ModifierParameter")

package com.perfomer.checkielite.common.ui.cui.widget.skeleton

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun CuiSkeletonBox(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    label: String = "Skeleton",
    content: @Composable CuiSkeletonBoxScope.() -> Unit
) {
    Box(
        contentAlignment = contentAlignment,
        modifier = modifier
    ) {
        val scope = remember {
            CuiSkeletonBoxScope(
                skeletonScope = CuiSkeletonScopeImpl(skeletonImpact = 0),
                boxScope = this,
            )
        }

        CuiSkeletonContainer(
            scope = scope,
            label = label,
            content = { content(scope) },
        )
    }
}


@Composable
fun CuiSkeletonColumn(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    label: String = "Skeleton",
    content: @Composable CuiSkeletonColumnScope.() -> Unit
) {
    Column(
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        modifier = modifier
    ) {
        val scope = remember {
            CuiSkeletonColumnScope(
                skeletonScope = CuiSkeletonScopeImpl(skeletonImpact = 1),
                columnScope = this,
            )
        }

        CuiSkeletonContainer(
            scope = scope,
            label = label,
            content = { content(scope) }
        )
    }
}

@Composable
fun CuiSkeletonRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    label: String = "Skeleton",
    content: @Composable CuiSkeletonRowScope.() -> Unit,
) {
    Row(
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
        modifier = modifier
    ) {
        val scope = remember {
            CuiSkeletonRowScope(
                skeletonScope = CuiSkeletonScopeImpl(skeletonImpact = 0),
                rowScope = this,
            )
        }

        CuiSkeletonContainer(
            scope = scope,
            label = label,
            content = { content(scope) },
        )
    }
}

@Composable
private fun CuiSkeletonContainer(
    scope: CuiSkeletonScope,
    label: String,
    content: @Composable CuiSkeletonScope.() -> Unit,
) {
    val transition = rememberInfiniteTransition(label = label + "Transition")
    val progress = transition.animateFloat(
        initialValue = 0F,
        targetValue = 1F,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = label + "Progress",
    )

    CompositionLocalProvider(
        LocalCuiSkeletonProgress provides progress
    ) {
        scope.resetSkeletonIndexes()
        scope.content()
    }
}