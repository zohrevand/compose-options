package io.github.zohrevand.options

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse

object OptionsDefaults {
    @Composable fun colors() = defaultOptionsColors

    @Composable fun colors(
        containerColor: Color = Color.Unspecified,
        thumbColor: Color = Color.Unspecified,
        activeTrackColor: Color = Color.Unspecified,
        inactiveTrackColor: Color = Color.Unspecified,
        trackTextColor: Color = Color.Unspecified,
        thumbTextColor: Color = Color.Unspecified,
    ) = defaultOptionsColors.copy(
        containerColor = containerColor,
        thumbColor = thumbColor,
        activeTrackColor = activeTrackColor,
        inactiveTrackColor = inactiveTrackColor,
        trackTextColor = trackTextColor,
        thumbTextColor = thumbTextColor,
    )

    internal val defaultOptionsColors: OptionsColors
        @Composable
        get() {
            val primaryColor = MaterialTheme.colorScheme.primary
            return OptionsColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                thumbColor = primaryColor,
                activeTrackColor = primaryColor.copy(alpha = 0.3f),
                inactiveTrackColor = primaryColor.copy(alpha = 0.1f),
                trackTextColor = MaterialTheme.colorScheme.onSurface,
                thumbTextColor = MaterialTheme.colorScheme.onPrimary,
            )
        }
}

@Immutable
class OptionsColors(
    val containerColor: Color,
    val thumbColor: Color,
    val activeTrackColor: Color,
    val inactiveTrackColor: Color,
    val trackTextColor: Color,
    val thumbTextColor: Color,
) {
    fun copy(
        containerColor: Color = this.containerColor,
        thumbColor: Color = this.thumbColor,
        activeTrackColor: Color = this.activeTrackColor,
        inactiveTrackColor: Color = this.inactiveTrackColor,
        trackTextColor: Color = this.trackTextColor,
        thumbTextColor: Color = this.thumbTextColor,
    ) =
        OptionsColors(
            containerColor.takeOrElse { this.containerColor },
            thumbColor.takeOrElse { this.thumbColor },
            activeTrackColor.takeOrElse { this.activeTrackColor },
            inactiveTrackColor.takeOrElse { this.inactiveTrackColor },
            trackTextColor.takeOrElse { this.trackTextColor },
            thumbTextColor.takeOrElse { this.thumbTextColor },
        )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is OptionsColors) return false

        if (containerColor != other.containerColor) return false
        if (thumbColor != other.thumbColor) return false
        if (activeTrackColor != other.activeTrackColor) return false
        if (inactiveTrackColor != other.inactiveTrackColor) return false
        if (trackTextColor != other.trackTextColor) return false
        if (thumbTextColor != other.thumbTextColor) return false

        return true
    }

    override fun hashCode(): Int {
        var result = containerColor.hashCode()
        result = 31 * result + thumbColor.hashCode()
        result = 31 * result + activeTrackColor.hashCode()
        result = 31 * result + inactiveTrackColor.hashCode()
        result = 31 * result + trackTextColor.hashCode()
        result = 31 * result + thumbTextColor.hashCode()
        return result
    }
}
