package io.github.first_uninteresting_username.projects_list.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun MyTextField(
    value: String,
    label: String,
    modifier: Modifier,
    onClick: (() -> Unit)? = null,
    textMinLines: Int = 1,
    textMaxLines: Int = textMinLines.coerceAtLeast(1),
) {
    Box(modifier = modifier) {
        val surfaceModifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .heightIn(56.dp)

        val shape = RoundedCornerShape(16.dp)
        val color = MaterialTheme.colorScheme.secondaryContainer

        val surfaceContent: @Composable () -> Unit = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp),
                    maxLines = textMaxLines,
                    minLines = textMinLines,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        if (onClick != null) {
            Surface(
                onClick = onClick,
                modifier = surfaceModifier,
                shape = shape,
                color = color,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                tonalElevation = 2.dp,
                shadowElevation = 4.dp
            ) { surfaceContent() }
        } else {
            Surface(
                modifier = surfaceModifier,
                shape = shape,
                color = color,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                tonalElevation = 2.dp,
                shadowElevation = 4.dp
            ) { surfaceContent() }
        }

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 16.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 4.dp)
        )
    }
}