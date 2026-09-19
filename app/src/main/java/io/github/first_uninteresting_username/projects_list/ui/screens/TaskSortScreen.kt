package io.github.first_uninteresting_username.projects_list.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import io.github.first_uninteresting_username.projects_list.R
import io.github.first_uninteresting_username.projects_list.data.model.SortOption
import io.github.first_uninteresting_username.projects_list.ui.components.ActionButton
import io.github.first_uninteresting_username.projects_list.ui.components.SimpleTopBar
import io.github.first_uninteresting_username.projects_list.ui.viewmodel.ProjectViewModel

@Composable
fun TaskSortScreen(navController: NavHostController, viewModel: ProjectViewModel) {
    val settings by viewModel.taskSearchSettings.collectAsState()

    var selectedSort by remember { mutableStateOf(settings.sortBy) }
    var descending by remember { mutableStateOf(settings.descending) }

    val applyAndGoBack = {
        viewModel.updateTaskSearchSettings(
            settings.copy(sortBy = selectedSort, descending = descending)
        )
        navController.popBackStack()
        Unit
    }

    BackHandler(onBack = applyAndGoBack)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            ActionButton(
                onClick = applyAndGoBack,
                icon = painterResource(R.drawable.ic_exit_to_app),
                contentDescription = "Apply sort"
            )
        },
        topBar = {
            SimpleTopBar(navController = navController, name = "Sort", onBack = applyAndGoBack)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top)
        ) {
            for (option in SortOption.entries) {
                SortOptionRow(
                    label = option.label,
                    selected = option == selectedSort,
                    onSelect = { selectedSort = option }
                )
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 1.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Descending order")
                Switch(checked = descending, onCheckedChange = { descending = it })
            }
        }
    }
}

@Composable
private fun SortOptionRow(
    label: String,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onSelect)
        Text(label)
    }
}
