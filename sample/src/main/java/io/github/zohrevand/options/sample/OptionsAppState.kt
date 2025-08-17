package io.github.zohrevand.options.sample

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.zohrevand.options.Option

@Composable
fun rememberOptionsAppState(
    radius: Dp = 16.dp,
    optionCount: Int = availableOptionCounts.last(),
    draggable: Boolean = true,
    activeTrackVisible: Boolean = true,
    inactiveTrackVisible: Boolean = false,
    primaryColor: Color = appColorList.first(),
): OptionsAppState {
    return remember {
        OptionsAppState(
            radius = radius,
            optionCount = optionCount,
            draggable = draggable,
            activeTrackVisible = activeTrackVisible,
            inactiveTrackVisible = inactiveTrackVisible,
            primaryColor = primaryColor,
        )
    }
}

class OptionsAppState(
    radius: Dp,
    optionCount: Int,
    draggable: Boolean,
    activeTrackVisible: Boolean,
    inactiveTrackVisible: Boolean,
    primaryColor: Color,
) {
    var radius by mutableStateOf(radius)
        private set

    var optionCount by mutableIntStateOf(optionCount)
        private set

    var draggable by mutableStateOf(draggable)
        private set

    var activeTrackVisible by mutableStateOf(activeTrackVisible)
        private set

    var inactiveTrackVisible by mutableStateOf(inactiveTrackVisible)
        private set

    var primaryColor by mutableStateOf(primaryColor)
        private set

    fun onUpdate(event: OptionsAppEvent) {
        when (event) {
            is OptionsAppEvent.RadiusChanged -> radius = event.radius
            is OptionsAppEvent.OptionCountChanged -> optionCount = event.optionCount
            is OptionsAppEvent.DraggableChanged -> draggable = event.draggable
            is OptionsAppEvent.PrimaryColorChanged -> primaryColor = event.primaryColor
            is OptionsAppEvent.ActiveTrackVisibleChanged ->
                activeTrackVisible = event.visible
            is OptionsAppEvent.InactiveTrackVisibleChanged ->
                inactiveTrackVisible = event.visible

        }
    }
}

sealed interface OptionsAppEvent {
    data class RadiusChanged(val radius: Dp) : OptionsAppEvent
    data class OptionCountChanged(val optionCount: Int) : OptionsAppEvent
    data class DraggableChanged(val draggable: Boolean) : OptionsAppEvent
    data class ActiveTrackVisibleChanged(val visible: Boolean) : OptionsAppEvent
    data class InactiveTrackVisibleChanged(val visible: Boolean) : OptionsAppEvent
    data class PrimaryColorChanged(val primaryColor: Color) : OptionsAppEvent
}

val options = listOf(
    Option("1"),
    Option("2"),
    Option("3"),
    Option("4"),
    Option("5"),
    Option("6"),
)

val availableOptionCounts = listOf(2, 3, 4, 5, 6)

val appColorList = listOf(
    Color(0xFF277da1), Color(0xFF577590), Color(0xFF4d908e), Color(0xFF43aa8b),
    Color(0xFF90be6d), Color(0xFFf9c74f), Color(0xFFf9844a), Color(0xFFf8961e),
    Color(0xFFf3722c), Color(0xFFf94144), Color(0xFFef476f), Color(0xFFf78c6b),
    Color(0xFFff5883), Color(0xFFae2d68), Color(0xFF660f56), Color(0xFF341671)
)
