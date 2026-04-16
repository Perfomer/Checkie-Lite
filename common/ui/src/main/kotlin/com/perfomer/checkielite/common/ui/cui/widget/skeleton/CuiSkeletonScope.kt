package com.perfomer.checkielite.common.ui.cui.widget.skeleton

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember

val LocalCuiSkeletonProgress = compositionLocalOf<State<Float>> { error("Not provided") }

@Stable
interface CuiSkeletonScope {

    fun resetSkeletonIndexes()

    fun nextSkeletonIndex(): Int
}

class CuiSkeletonBoxScope internal constructor(
    skeletonScope: CuiSkeletonScope,
    boxScope: BoxScope,
) : CuiSkeletonScope by skeletonScope, BoxScope by boxScope

class CuiSkeletonColumnScope internal constructor(
    skeletonScope: CuiSkeletonScope,
    columnScope: ColumnScope,
) : CuiSkeletonScope by skeletonScope, ColumnScope by columnScope

class CuiSkeletonRowScope internal constructor(
    skeletonScope: CuiSkeletonScope,
    rowScope: RowScope,
) : CuiSkeletonScope by skeletonScope, RowScope by rowScope

internal class CuiSkeletonScopeImpl(
    private val skeletonImpact: Int,
) : CuiSkeletonScope {

    private var skeletonIndex: Int = 0

    override fun resetSkeletonIndexes() {
        skeletonIndex = 0
    }

    override fun nextSkeletonIndex(): Int {
        val currentIndex = skeletonIndex
        skeletonIndex += skeletonImpact
        return currentIndex
    }
}

@Composable
internal fun CuiSkeletonScope.rememberSkeletonIndex(): Int {
    return remember { nextSkeletonIndex() }
}