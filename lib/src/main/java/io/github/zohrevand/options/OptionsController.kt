package io.github.zohrevand.options

import androidx.compose.animation.core.animate
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.util.fastCoerceIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun rememberOptionsController(
    thumbLabels: List<String>,
    selectedOptionIndex: Int,
    onOptionIndexChange: (Int) -> Unit,
    maxWidth: Dp,
    maxHeight: Dp,
    contentPadding: PaddingValues,
): OptionsController {
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current

    val scope = rememberCoroutineScope()

    val dimensions = rememberDimensions(
        maxWidth = maxWidth,
        maxHeight = maxHeight,
        contentPadding = contentPadding,
        optionCount = thumbLabels.size,
        layoutDirection = layoutDirection,
        density = density,
    )

    val optionsController = remember(dimensions) {
        OptionsController(
            onOptionIndexChange = onOptionIndexChange,
            dimensions = dimensions,
            thumbLabels = thumbLabels,
            coroutineScope = scope,
            density = density,
        ).apply {
            updateOptionIndex(selectedOptionIndex, animate = false)
        }
    }

    return optionsController
}

internal class OptionsController internal constructor(
    val dimensions: Dimensions,
    val thumbLabels: List<String>,
    val onOptionIndexChange: (Int) -> Unit,
    private val coroutineScope: CoroutineScope,
    private val density: Density,
) {
    var isDragging by mutableStateOf(false)
        private set

    var offsetX by mutableFloatStateOf(dimensions.startPaddingPx)
        private set

    var thumbLabel by mutableStateOf(thumbLabels.first())
        private set

    private var selectedOptionIndex: Int = 0

    val activeTrackWidth: Dp
        get() = with(density) { offsetX.toDp() + (dimensions.sectionWidth / 2) }

    val inactiveTrackWidth: Dp
        get() = with(density) {
            (dimensions.maxWidthPx - offsetX).toDp() - (dimensions.sectionWidth / 2)
        }

    fun updateOptionIndex(index: Int, animate: Boolean = true) {
        selectedOptionIndex = index
        updateThumbLabel(index)
        coroutineScope.launch {
            val targetOffset = dimensions.calculateOffset(index)
            if (animate) animateToOffset(targetOffset) else offsetX = targetOffset
        }
        onOptionIndexChange(selectedOptionIndex)
    }

    fun onDragStart() { isDragging = true }

    fun onDrag(dragAmount: Float) {
        offsetX = (offsetX + dragAmount).fastCoerceIn(0f, dimensions.maxOffset)
        selectedOptionIndex = dimensions.nearestIndex(offsetX)
        updateThumbLabel(selectedOptionIndex)
        onOptionIndexChange(selectedOptionIndex)
    }

    fun onDragEnd(onOptionIndexChange: (Int) -> Unit) {
        isDragging = false
        onOptionIndexChange(selectedOptionIndex)
        updateOptionIndex(selectedOptionIndex, animate = true)
    }

    private suspend fun animateToOffset(targetOffset: Float) {
        animate(initialValue = offsetX, targetValue = targetOffset) { value, _ ->
            offsetX = value
        }
    }

    private fun updateThumbLabel(index: Int) {
        thumbLabel = thumbLabels[index]
    }
}
