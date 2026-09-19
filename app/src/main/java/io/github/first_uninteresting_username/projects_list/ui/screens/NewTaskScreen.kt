package io.github.first_uninteresting_username.projects_list.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import io.github.first_uninteresting_username.projects_list.R
import io.github.first_uninteresting_username.projects_list.data.model.Task
import io.github.first_uninteresting_username.projects_list.ui.components.ActionButton
import io.github.first_uninteresting_username.projects_list.ui.components.ProjectEditingColumn
import io.github.first_uninteresting_username.projects_list.ui.viewmodel.ProjectViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTaskScreen(
    navController: NavHostController,
    viewModel: ProjectViewModel,
    projectId: String?,
) {
    var priorityFloat by remember { mutableFloatStateOf(0f) }
    var motivationFloat by remember { mutableFloatStateOf(0f) }
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var link by remember { mutableStateOf("") }
    var favorite by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Adding new task") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = "Back",
                        )
                    }
                },
                actions = {
                    if (favorite) {
                        IconButton(onClick = { favorite = !favorite }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_filled_star),
                                contentDescription = "Mark $name as not favorite"
                            )
                        }
                    } else {
                        IconButton(onClick = { favorite = !favorite }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_star),
                                contentDescription = "Mark $name as favorite"
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            ActionButton(
                onClick = {
                    if (projectId != null && name.isNotBlank()) {
                        val project = viewModel.projects.value.find { it.uuid == projectId }
                        val nextChronology = (project?.tasks?.maxOfOrNull { it.chronology } ?: 0) + 1
                        val task = Task(
                            chronology = nextChronology,
                            title = name,
                            description = description,
                            priority = priorityFloat,
                            motivation = motivationFloat,
                            favorite = favorite,
                        )
                        viewModel.addTask(projectId, task)
                        navController.popBackStack()
                    }
                },
                icon = painterResource(R.drawable.ic_add_task),
                contentDescription = "Add the task",
            )
        }
    ) { innerPadding ->
        Column(Modifier.verticalScroll(rememberScrollState())) {
            ProjectEditingColumn(
                modifier = Modifier.padding(innerPadding),
                nameValue = name,
                onNameValueChange = { name = it },
                descriptionValue = description,
                onDescriptionValueChange = { description = it },
                linkValue = link,
                onLinkValueChange = { link = it },
                priorityValue = priorityFloat,
                onPriorityChange = { priorityFloat = it },
                motivationValue = motivationFloat,
                onMotivationChange = { motivationFloat = it }
            )
        }
    }
}
