package io.github.zohrevand.options.sample.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    onSurfaceVariant = Color(0xFFADADAD),
    outline = Color.DarkGray,
    outlineVariant = Color(0xFF212121),
)

private val LightColorScheme = lightColorScheme(
    onSurfaceVariant = Color(0xFF8C8C8C),
    outline = Color.LightGray,
    outlineVariant = Color(0xFFE7E7E7),
)

@Composable
fun ComposesOptionsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
