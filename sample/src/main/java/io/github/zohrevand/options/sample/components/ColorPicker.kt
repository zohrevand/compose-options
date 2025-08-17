package io.github.zohrevand.options.sample.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import kotlin.collections.chunked
import kotlin.collections.forEach

@Composable
fun ColorPicker(
    colorList: List<Color>,
    primaryColor: Color,
    onPrimaryColorChange: (Color) -> Unit,
    modifier: Modifier = Modifier,
    columns: Int = 8,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    shape: Shape = MaterialTheme.shapes.small,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(containerColor, shape)
    ) {
        colorList.chunked(columns).forEach { colorItems ->
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                colorItems.forEach { color ->
                    ColorItem(
                        color = color,
                        onSelectColor = onPrimaryColorChange,
                        isSelected = color == primaryColor,
                        modifier = Modifier.weight(1f)
                    )
                }

                repeat(columns - colorItems.size) {
                    Box(Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun ColorItem(
    color: Color,
    onSelectColor: (Color) -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    val selectionBorderModifier = if (isSelected) {
        Modifier.border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
    } else {
        Modifier
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .aspectRatio(1f)
            .padding(5.dp)
            .then(selectionBorderModifier)
            .clickable(showRipple = false) { onSelectColor(color) }
    ) {
        Box(
            Modifier
                .size(18.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(color)
        )
    }
}

fun Modifier.clickable(
    enabled: Boolean = true,
    showRipple: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit,
): Modifier = composed {
    clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = if (showRipple) LocalIndication.current else null,
        enabled = enabled,
        onClickLabel = onClickLabel,
        role = role,
        onClick = onClick,
    )
}
