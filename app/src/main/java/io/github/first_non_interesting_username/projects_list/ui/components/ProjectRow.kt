package io.github.first_non_interesting_username.projects_list.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.github.first_non_interesting_username.projects_list.R

@Composable
fun ProjectRow(
    name: String,
    isFavorite: Boolean,
    modifier: Modifier = Modifier.Companion,
    priority: Float,
    motivation: Float,
    chronology: Int,
    onClick: () -> Unit = {},
) {
    val motivation = motivation.toInt()
    val priority = priority.toInt()

    Row(
        modifier = modifier.then(
            Modifier
                .fillMaxWidth(0.9f)
                .height(64.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 16.dp)
                .clickable {
                    onClick()
                }),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "#$chronology",
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
        Text(
            text = "Mot: $motivation/10",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
        Text(
            text = "Pri: $priority/10",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
        if (isFavorite) {
            Icon(
                painter = painterResource(R.drawable.ic_filled_star),
                contentDescription = "$name is marked as favorite",
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        } else {
            Icon(
                painter = painterResource(R.drawable.ic_star),
                contentDescription = "$name is not marked as favorite",
                tint = MaterialTheme.colorScheme.onPrimaryContainer,g
            )
        }
    }
}
