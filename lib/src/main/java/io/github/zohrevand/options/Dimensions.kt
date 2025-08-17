package io.github.zohrevand.options

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.util.fastCoerceIn
import androidx.compose.ui.util.fastRoundToInt

@Composable
internal fun rememberDimensions(
    maxWidth: Dp,
    maxHeight: Dp,
    contentPadding: PaddingValues,
    optionCount: Int,
    layoutDirection: LayoutDirection,
    density: Density,
): Dimensions =
    remember(maxWidth, maxHeight, contentPadding, optionCount, layoutDirection, density) {
        with(density) {
            val startPadding = contentPadding.calculateStartPadding(layoutDirection)
            val endPadding = contentPadding.calculateEndPadding(layoutDirection)
            val horizontalPadding = startPadding + endPadding
            val verticalPadding =
                contentPadding.calculateTopPadding() + contentPadding.calculateBottomPadding()

            val sectionWidth = (maxWidth - horizontalPadding) / optionCount
            val thumbWidth = sectionWidth
            val thumbHeight = maxHeight - verticalPadding
            val maxSectionWidth = sectionWidth + verticalPadding

            val sectionWidthPx = sectionWidth.toPx()
            val thumbWidthPx = thumbWidth.toPx()
            val startPaddingPx = startPadding.toPx()
            val verticalPaddingPx = verticalPadding.toPx()
            val maxWidthPx = maxWidth.toPx()

            val maxOffset = maxWidthPx - thumbWidthPx - verticalPaddingPx

            Dimensions(
                sectionWidth = sectionWidth,
                maxSectionWidth = maxSectionWidth,
                thumbWidth = thumbWidth,
                thumbHeight = thumbHeight,
                verticalPaddingPx = verticalPaddingPx,
                horizontalPadding = horizontalPadding,
                startPaddingPx = startPaddingPx,
                sectionWidthPx = sectionWidthPx,
                thumbWidthPx = thumbWidthPx,
                maxOffset = maxOffset,
                maxWidthPx = maxWidthPx,
                maxHeight = maxHeight,
                contentPadding = contentPadding,
            )
        }
    }

@Stable
internal data class Dimensions(
    val sectionWidth: Dp,
    val maxSectionWidth: Dp,
    val thumbWidth: Dp,
    val thumbHeight: Dp,
    val verticalPaddingPx: Float,
    val horizontalPadding: Dp,
    val startPaddingPx: Float,
    val sectionWidthPx: Float,
    val thumbWidthPx: Float,
    val maxOffset: Float,
    val maxWidthPx: Float,
    val maxHeight: Dp,
    val contentPadding: PaddingValues,
) {
    fun calculateOffset(index: Int): Float = startPaddingPx + (sectionWidthPx * index)

    fun nearestIndex(offset: Float): Int =
        ((offset - startPaddingPx) / sectionWidthPx)
            .fastRoundToInt()
            .fastCoerceIn(0, (maxWidthPx / sectionWidthPx).toInt() - 1)
}
