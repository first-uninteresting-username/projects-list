package io.github.first_uninteresting_username.projects_list.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import io.github.first_uninteresting_username.projects_list.util.openUrl


@Composable
fun ProjectDisplayColumn(
    nameValue: String,
    descriptionValue: String,
    linkValue: String,
    priorityValue: Float,
    motivationValue: Float,
    modifier: Modifier,
) {
    val context = LocalContext.current
    Column(
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .padding(4.dp),
        ) ) {
        MyTextField(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .align(Alignment.CenterHorizontally),
            value = nameValue,
            label = "Name"
            )
        Spacer(Modifier.height(8.dp))
        MyTextField(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .align(Alignment.CenterHorizontally),
            value = descriptionValue,
            label = "Description",
            textMinLines = 3,
            textMaxLines = 6,
        )
        Spacer(Modifier.height(8.dp))
        MyTextField(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .align(Alignment.CenterHorizontally),
            value = linkValue,
            label = "Link",
            onClick = {context.openUrl(linkValue)}
        )
        Spacer(Modifier.height(8.dp))
        SliderWithDescription(
            value = priorityValue,
            enabled = false,
            text = "Priority",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.9f),
            textModifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.9f),
            keyModifier = Modifier

                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.9f),
        )
        Spacer(Modifier.height(8.dp))
        SliderWithDescription(
            value = motivationValue,
            enabled = false,
            text = "Motivation",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.9f),
            textModifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.9f),
            keyModifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.9f),
        )

    }
}

