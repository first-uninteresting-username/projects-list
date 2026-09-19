package io.github.first_uninteresting_username.projects_list.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun ConfirmationButton(
    icon: Painter? = null,
    text: String? = null,
    clickedText: String? = null,
    contentDescription: String? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    clicked: Boolean = false
) {
    val label = if (clicked) clickedText ?: text else text

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onClick,
            modifier = modifier
                .widthIn(min = 200.dp)
                .heightIn(min = 64.dp),
            shape = RoundedCornerShape(100.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (clicked) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.primary
                },
                contentColor = if (clicked) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onPrimary
                }
            ),
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 20.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = if (clicked) 0.dp else 4.dp
            )
        ) {
            if (icon != null) {
                Icon(
                    painter = icon,
                    contentDescription = contentDescription,
                    modifier = Modifier.alpha(if (clicked) 0.7f else 1f)
                )
                if (label != null) {
                    Spacer(modifier = Modifier.width(12.dp))
                }
            }
            label?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.alpha(if (clicked) 0.7f else 1f)
                )
            }
        }
    }
}