package io.github.first_uninteresting_username.projects_list.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SliderWithDescription(
    value: Float,
    modifier: Modifier,
    onValueChange: (Float) -> Unit = {},
    textModifier: Modifier,
    text: String,
    keyModifier: Modifier,
    enabled: Boolean = true,
) {

    Text(
        text = text,
        modifier = textModifier
    )
    Slider(
        value = value,
        onValueChange = onValueChange,
        valueRange = 0f..10f,
        steps = 9,
        modifier = modifier,
        enabled = enabled,
    )

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = keyModifier,
    ) {
        for (i in 0..10) {
            Text(text = i.toString())
        }
    }
}