package io.github.zohrevand.options

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.compose.ui.util.fastMap

/**
 * Creates and remembers an [OptionsRowState] for managing the state of [OptionsRow].
 *
 * This should be used at the call site to hold the current selection and handle option changes.
 *
 * @param options The list of available [Option]s to display.
 * @param selectedOptionIndex The index of the currently selected option.
 * @param onOptionIndexChange Callback invoked when the selected option index changes.
 *
 * @return A remembered [OptionsRowState] that can be passed to [OptionsRow].
 */
@Composable
fun rememberOptionsRowState(
    options: List<Option>,
    selectedOptionIndex: Int = 0,
    onOptionIndexChange: (Int) -> Unit = {},
): OptionsRowState {
    return remember(options, selectedOptionIndex, onOptionIndexChange) {
        OptionsRowState(options, selectedOptionIndex, onOptionIndexChange)
    }
}

/**
 * State holder for [OptionsRow].
 *
 * Contains the list of [options], the currently selected index, and a callback
 * for when the selection changes. Use [rememberOptionsRowState] to create
 * and retain this state across recompositions.
 *
 * @property options The list of available [Option]s to display.
 * @property selectedOptionIndex The index of the currently selected option.
 * @property onOptionIndexChange Callback invoked when the selected option index changes.
 */
@Stable
class OptionsRowState internal constructor(
    val options: List<Option>,
    val selectedOptionIndex: Int,
    val onOptionIndexChange: (Int) -> Unit,
)

/**
 * A composable row of selectable [Option]s with a draggable thumb to indicate the current selection.
 *
 * The selection state is controlled by [state], typically created via [rememberOptionsRowState].
 *
 * @param state The [OptionsRowState] that holds the current options and selection.
 * @param modifier Modifier to be applied to the layout.
 * @param trackTextStyle [TextStyle] applied to the text shown on the track.
 * @param thumbTextStyle [TextStyle] applied to the text shown on the thumb.
 * @param colors The color scheme for the track and thumb.
 * @param activeTrackVisible Whether to display the active portion of the track.
 * @param inactiveTrackVisible Whether to display the inactive portion of the track.
 * @param draggable Whether the thumb can be dragged to change the selection.
 * @param containerHeight The height of the entire row container.
 * @param shape The shape of the row container.
 * @param contentPadding Padding around the contents of the row.
 */
@Composable
fun OptionsRow(
    state: OptionsRowState,
    modifier: Modifier = Modifier,
    trackTextStyle: TextStyle = LocalTextStyle.current,
    thumbTextStyle: TextStyle = LocalTextStyle.current,
    colors: OptionsColors = OptionsDefaults.colors(),
    activeTrackVisible: Boolean = true,
    inactiveTrackVisible: Boolean = false,
    draggable: Boolean = true,
    containerHeight: Dp = 64.dp,
    shape: Shape = MaterialTheme.shapes.medium,
    contentPadding: PaddingValues = PaddingValues(8.dp),
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(containerHeight)
            .background(colors.containerColor, shape),
    ) {
        val controller = rememberOptionsController(
            selectedOptionIndex = state.selectedOptionIndex,
            onOptionIndexChange = state.onOptionIndexChange,
            maxWidth = this.maxWidth,
            maxHeight = containerHeight,
            thumbLabels = state.options.map { it.thumbText },
            contentPadding = contentPadding,
        )

        if (activeTrackVisible) {
            ActiveTrackBackground(
                modifier = Modifier.align(Alignment.CenterStart),
                width = controller.activeTrackWidth,
                contentPadding = controller.dimensions.contentPadding,
                activeTrackColor = colors.activeTrackColor,
                shape = shape,
            )
        }

        if (inactiveTrackVisible) {
            InactiveTrackBackground(
                modifier = Modifier.align(Alignment.CenterEnd),
                width = controller.inactiveTrackWidth,
                contentPadding = controller.dimensions.contentPadding,
                inactiveTrackColor = colors.inactiveTrackColor,
                shape = shape,
            )
        }

        Track(
            sectionWidth = controller.dimensions.sectionWidth,
            contentPadding = controller.dimensions.contentPadding,
            trackTexts = state.options.fastMap { it.trackText },
            textStyle = trackTextStyle,
            textColor = colors.trackTextColor,
            onItemClick = { controller.updateOptionIndex(it) },
        )

        Thumb(
            modifier = Modifier.align(Alignment.CenterStart),
            offsetX = controller.offsetX,
            dimensions = controller.dimensions,
            baseWidth = controller.dimensions.thumbWidth,
            baseHeight = controller.dimensions.thumbHeight,
            maxWidth = controller.dimensions.maxSectionWidth,
            maxHeight = controller.dimensions.maxHeight,
            thumbColor = colors.thumbColor,
            thumbLabel = controller.thumbLabel,
            isDragging = controller.isDragging,
            textStyle = thumbTextStyle,
            textColor = colors.thumbTextColor,
            shape = shape,
            draggable = draggable,
            onDragStart = { controller.onDragStart() },
            onDrag = { controller.onDrag(it) },
            onDragEnd = { controller.onDragEnd(state.onOptionIndexChange) },
        )
    }
}

@Composable
private fun Track(
    sectionWidth: Dp,
    contentPadding: PaddingValues,
    trackTexts: List<String>,
    textColor: Color,
    textStyle: TextStyle,
    onItemClick: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        trackTexts.fastForEachIndexed { index, label ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(sectionWidth)
                    .clickable(
                        onClick = { onItemClick(index) },
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    )
            ) {
                Text(text = label, style = textStyle, color = textColor)
            }
        }
    }
}

@Composable
private fun ActiveTrackBackground(
    width: Dp,
    contentPadding: PaddingValues,
    activeTrackColor: Color,
    shape: Shape,
    modifier: Modifier = Modifier,
) {
    val activeTrackFillShape = if (shape is RoundedCornerShape) {
        RoundedCornerShape(
            topStart = shape.topStart,
            topEnd = CornerSize(0),
            bottomEnd = CornerSize(0),
            bottomStart = shape.bottomEnd,
        )
    } else shape

    Box(
        modifier = modifier
            .fillMaxHeight()
            .widthIn(min = 50.dp)
            .width(width)
            .padding(contentPadding)
            .background(activeTrackColor, activeTrackFillShape),
    )
}

@Composable
private fun InactiveTrackBackground(
    width: Dp,
    contentPadding: PaddingValues,
    inactiveTrackColor: Color,
    shape: Shape,
    modifier: Modifier = Modifier,
) {
    val inactiveTrackFillShape = if (shape is RoundedCornerShape) {
        RoundedCornerShape(
            topStart = CornerSize(0),
            topEnd = shape.topEnd,
            bottomEnd = shape.bottomEnd,
            bottomStart = CornerSize(0),
        )
    } else shape

    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(width)
            .padding(contentPadding)
            .background(inactiveTrackColor, inactiveTrackFillShape),
    )
}

@Composable
private fun Thumb(
    dimensions: Dimensions,
    offsetX: Float,
    baseWidth: Dp,
    baseHeight: Dp,
    maxWidth: Dp,
    maxHeight: Dp,
    thumbColor: Color,
    shape: Shape,
    thumbLabel: String,
    textStyle: TextStyle,
    textColor: Color,
    draggable: Boolean,
    isDragging: Boolean,
    onDragStart: () -> Unit,
    onDrag: (Float) -> Unit,
    onDragEnd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .thumbSize(
                isDragging = isDragging,
                baseWidth = baseWidth,
                baseHeight = baseHeight,
                maxWidth = maxWidth,
                maxHeight = maxHeight
            )
            .offset { IntOffset(offsetX.toInt(), 0) }
            .clip(shape)
            .background(thumbColor)
            .pointerInput(dimensions, draggable) {
                if (draggable) {
                    detectHorizontalDragGestures(
                        onDragStart = { onDragStart() },
                        onHorizontalDrag = { _, dragAmount -> onDrag(dragAmount) },
                        onDragEnd = { onDragEnd() }
                    )
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = thumbLabel,
            style = textStyle,
            color = textColor,
        )
    }
}

private fun Modifier.thumbSize(
    isDragging: Boolean,
    baseWidth: Dp,
    baseHeight: Dp,
    maxWidth: Dp,
    maxHeight: Dp,
): Modifier = composed {
    val animatedHeight by animateDpAsState(
        targetValue = if (isDragging) maxHeight else baseHeight,
        label = "ThumbHeightAnimation",
    )

    val animatedWidth by animateDpAsState(
        targetValue = if (isDragging) maxWidth else baseWidth,
        label = "ThumbWidthAnimation",
    )

    height(animatedHeight).width(animatedWidth)
}

@Preview
@Composable
private fun OptionsRowPreview() {
    val options = listOf(Option("1"), Option("2"), Option("3"), Option("4"), Option("5"))

    MaterialTheme {
        OptionsRow(
            state = rememberOptionsRowState(options)
        )
    }
}
