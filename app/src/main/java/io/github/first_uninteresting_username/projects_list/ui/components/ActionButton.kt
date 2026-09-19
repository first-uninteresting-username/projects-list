package io.github.first_uninteresting_username.projects_list.ui.components

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter

@Composable
fun ActionButton(
    onClick: () -> Unit,
    icon: Painter,
    contentDescription: String,
) {
    FloatingActionButton(
        onClick = onClick
    ) {
        Icon(
            painter = icon,
            contentDescription = contentDescription
        )
    }
}