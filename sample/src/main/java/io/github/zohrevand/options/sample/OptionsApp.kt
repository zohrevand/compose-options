package io.github.zohrevand.options.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.zohrevand.options.Option
import io.github.zohrevand.options.OptionsDefaults
import io.github.zohrevand.options.OptionsRow
import io.github.zohrevand.options.rememberOptionsRowState
import io.github.zohrevand.options.sample.components.ColorPicker
import io.github.zohrevand.options.sample.components.CustomHorizontalDivider
import io.github.zohrevand.options.sample.components.CustomSlider
import io.github.zohrevand.options.sample.components.HeadlineText

@Composable
fun OptionsApp() {
    val state = rememberOptionsAppState()

    Scaffold(Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            OptionsDemo(state)

            OptionsSetting(
                modifier = Modifier.weight(1f),
                state = state,
                onUpdate = state::onUpdate,
            )
        }
    }
}

@Composable
private fun OptionsDemo(
    optionAppState: OptionsAppState,
    modifier: Modifier = Modifier,
) {
    val optionsState = rememberOptionsRowState(
        options = options.take(optionAppState.optionCount),
        selectedOptionIndex = 0,
        onOptionIndexChange = {},
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp),
        contentAlignment = Alignment.Center,
    ) {
        OptionsRow(
            state = optionsState,
            activeTrackVisible = optionAppState.activeTrackVisible,
            inactiveTrackVisible = optionAppState.inactiveTrackVisible,
            draggable = optionAppState.draggable,
            shape = RoundedCornerShape(optionAppState.radius),
            colors = OptionsDefaults.colors(
                thumbColor = optionAppState.primaryColor,
                activeTrackColor = optionAppState.primaryColor.copy(alpha = 0.3f),
                inactiveTrackColor = optionAppState.primaryColor.copy(alpha = 0.1f),
            ),
            thumbTextStyle = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OptionsSetting(
    state: OptionsAppState,
    onUpdate: (OptionsAppEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        DraggableSettings(
            draggable = state.draggable,
            onDraggableChange = { onUpdate(OptionsAppEvent.DraggableChanged(it)) },
            primaryColor = state.primaryColor,
        )

        CustomHorizontalDivider()

        ActiveTrackVisibleSettings(
            activeTrackVisible = state.activeTrackVisible,
            onActiveTrackVisibleChange = {
                onUpdate(OptionsAppEvent.ActiveTrackVisibleChanged(it))
            },
            primaryColor = state.primaryColor,
        )

        CustomHorizontalDivider()

        InActiveTrackVisibleSettings(
            inactiveTrackVisible = state.inactiveTrackVisible,
            onInactiveTrackVisibleChange = {
                onUpdate(OptionsAppEvent.InactiveTrackVisibleChanged(it))
            },
            primaryColor = state.primaryColor,
        )

        CustomHorizontalDivider()

        RadiusSettings(
            radius = state.radius,
            onRadiusChange = { onUpdate(OptionsAppEvent.RadiusChanged(it)) },
            primaryColor = state.primaryColor,
        )

        CustomHorizontalDivider()

        OptionCountSettings(
            optionCount = state.optionCount,
            onOptionCountChange = { onUpdate(OptionsAppEvent.OptionCountChanged(it)) },
            primaryColor = state.primaryColor,
        )

        CustomHorizontalDivider()

        ColorSettings(
            primaryColor = state.primaryColor,
            onPrimaryColorChange = { onUpdate(OptionsAppEvent.PrimaryColorChanged(it)) },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RadiusSettings(
    radius: Dp,
    onRadiusChange: (Dp) -> Unit,
    primaryColor: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        HeadlineText(stringResource(R.string.shape_radius))

        CustomSlider(
            value = radius.value,
            onValueChange = { onRadiusChange(it.dp) },
            valueRange = 0f..32f,
            primaryColor = primaryColor,
        )
    }
}

@Composable
private fun OptionCountSettings(
    optionCount: Int,
    onOptionCountChange: (Int) -> Unit,
    primaryColor: Color,
    modifier: Modifier = Modifier,
) {
    val optionsState = rememberOptionsRowState(
        options = availableOptionCounts.map { Option(it.toString()) },
        selectedOptionIndex = availableOptionCounts.indexOf(optionCount),
        onOptionIndexChange = { onOptionCountChange(availableOptionCounts[it]) },
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        HeadlineText(stringResource(R.string.option_count))

        OptionsRow(
            state = optionsState,
            activeTrackVisible = false,
            draggable = false,
            colors = OptionsDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                thumbColor = primaryColor,
                activeTrackColor = primaryColor.copy(alpha = 0.3f),
                inactiveTrackColor = primaryColor.copy(alpha = 0.1f),
            )
        )
    }
}

@Composable
private fun DraggableSettings(
    draggable: Boolean,
    onDraggableChange: (Boolean) -> Unit,
    primaryColor: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HeadlineText(stringResource(R.string.draggable))

        Checkbox(
            checked = draggable,
            onCheckedChange = onDraggableChange,
            colors = CheckboxDefaults.colors(checkedColor = primaryColor),
        )
    }
}

@Composable
private fun ActiveTrackVisibleSettings(
    activeTrackVisible: Boolean,
    onActiveTrackVisibleChange: (Boolean) -> Unit,
    primaryColor: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HeadlineText(stringResource(R.string.active_track_visible))

        Checkbox(
            checked = activeTrackVisible,
            onCheckedChange = onActiveTrackVisibleChange,
            colors = CheckboxDefaults.colors(checkedColor = primaryColor),
        )
    }
}

@Composable
private fun InActiveTrackVisibleSettings(
    inactiveTrackVisible: Boolean,
    onInactiveTrackVisibleChange: (Boolean) -> Unit,
    primaryColor: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HeadlineText(stringResource(R.string.Inactive_track_visible))

        Checkbox(
            checked = inactiveTrackVisible,
            onCheckedChange = onInactiveTrackVisibleChange,
            colors = CheckboxDefaults.colors(checkedColor = primaryColor),
        )
    }
}

@Composable
private fun ColorSettings(
    primaryColor: Color,
    onPrimaryColorChange: (Color) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        HeadlineText(stringResource(R.string.color))

        ColorPicker(
            colorList = appColorList,
            primaryColor = primaryColor,
            onPrimaryColorChange = onPrimaryColorChange,
        )
    }
}
