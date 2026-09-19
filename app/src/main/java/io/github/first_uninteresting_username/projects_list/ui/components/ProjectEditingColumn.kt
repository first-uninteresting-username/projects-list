package io.github.first_uninteresting_username.projects_list.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProjectEditingColumn(
    modifier: Modifier,
    nameValue: String,
    onNameValueChange: (String) -> Unit,
    descriptionValue: String,
    onDescriptionValueChange: (String) -> Unit,
    linkValue: String,
    onLinkValueChange: (String) -> Unit,
    priorityValue: Float,
    onPriorityChange: (Float) -> Unit,
    motivationValue: Float,
    onMotivationChange: (Float) -> Unit,
)
{
    Column(
        modifier = modifier.then(
            Modifier
                .fillMaxSize()
                .padding(4.dp),
        )
    ) {
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = nameValue,
            onValueChange = onNameValueChange,
            label = { Text("Name") },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.9f),
            maxLines = 1,
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = descriptionValue,
            onValueChange = onDescriptionValueChange,
            label = { Text("Description") },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.9f),
            minLines = 3,
            maxLines = 6,
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = linkValue,
            onValueChange = onLinkValueChange,
            label = { Text("Link") },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.9f),
            maxLines = 1,
        )
        Spacer(Modifier.height(8.dp))
        SliderWithDescription(
            value = priorityValue,
            onValueChange = onPriorityChange,
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
            onValueChange = onMotivationChange,
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