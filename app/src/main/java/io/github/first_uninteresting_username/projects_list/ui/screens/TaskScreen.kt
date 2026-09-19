package io.github.first_uninteresting_username.projects_list.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import io.github.first_uninteresting_username.projects_list.R
import io.github.first_uninteresting_username.projects_list.Routes
import io.github.first_uninteresting_username.projects_list.ui.components.ActionButton
import io.github.first_uninteresting_username.projects_list.ui.components.AlertDialogExample
import io.github.first_uninteresting_username.projects_list.ui.components.ConfirmationButton
import io.github.first_uninteresting_username.projects_list.ui.components.ProjectDisplayColumn
import io.github.first_uninteresting_username.projects_list.ui.viewmodel.ProjectViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    navController: NavHostController,
    viewModel: ProjectViewModel,
    projectId: String?,
    taskId: String?,
) {
    val projects by viewModel.projects.collectAsState()
    val project = projects.find { it.uuid == projectId }
    val task = project?.tasks?.find { it.uuid == taskId }

    var priorityFloat by remember(taskId) { mutableFloatStateOf(task?.priority ?: 0f) }
    var motivationFloat by remember(taskId) { mutableFloatStateOf(task?.motivation ?: 0f) }
    var name by remember(taskId) { mutableStateOf(task?.title ?: "") }
    var description by remember(taskId) { mutableStateOf(task?.description ?: "") }
    var finished by remember(taskId) { mutableStateOf(task?.finished ?: false) }
    var confirmationDialog by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var deletionDialog by remember { mutableStateOf(false) }
    var favorite by remember(taskId) { mutableStateOf(task?.favorite ?: false) }

    val toggleFavourite: () -> Unit = {
        favorite = !favorite
        if (projectId != null && task != null) {
            viewModel.updateTask(projectId, task.copy(favorite = favorite))
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(name) },
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
                        IconButton(onClick = { toggleFavourite() }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_filled_star),
                                contentDescription = "Mark $name as not favorite"
                            )
                        }
                    } else {
                        IconButton(onClick = { toggleFavourite() }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_star),
                                contentDescription = "Mark $name as favorite"
                            )
                        }
                    }
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_more_vert),
                            contentDescription = "More options"
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Delete") },
                            onClick = {
                                expanded = false
                                deletionDialog = !deletionDialog
                            }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ActionButton(
                onClick = {
                    if (projectId != null && taskId != null) {
                        navController.navigate(Routes.editTaskRoute(projectId, taskId))
                    }
                },
                icon = painterResource(R.drawable.ic_edit),
                contentDescription = "Edit the task",
            )
        }
    ) { innerPadding ->
        Column(Modifier.verticalScroll(rememberScrollState())) {
            ProjectDisplayColumn(
                modifier = Modifier.padding(innerPadding),
                nameValue = name,
                descriptionValue = description,
                linkValue = "",
                priorityValue = priorityFloat,
                motivationValue = motivationFloat,
            )
            Spacer(Modifier.height(8.dp))
            ConfirmationButton(
                icon = painterResource(R.drawable.ic_done_all),
                text = "Mark as finished",
                clickedText = "Mark as unfinished",
                contentDescription = "Toggle state of completion of the task",
                onClick = { confirmationDialog = !confirmationDialog },
                clicked = finished
            )
        }
        if (confirmationDialog) {
            AlertDialogExample(
                onDismissRequest = { confirmationDialog = !confirmationDialog },
                onConfirmation = {
                    confirmationDialog = !confirmationDialog
                    finished = !finished
                    if (projectId != null && task != null) {
                        viewModel.updateTask(projectId, task.copy(finished = finished))
                    }
                },
                dialogTitle = if (!finished) {
                    "Mark as finished"
                } else {
                    "Mark as unfinished"
                },
                dialogText = if (!finished) {
                    "Do you want to mark task $name as finished?"
                } else {
                    "Do you want to mark task $name as unfinished?"
                },
                icon = painterResource(R.drawable.ic_done_all)
            )
        }
        if (deletionDialog) {
            AlertDialogExample(
                onDismissRequest = { deletionDialog = !deletionDialog },
                onConfirmation = {
                    deletionDialog = !deletionDialog
                    if (projectId != null && taskId != null) {
                        viewModel.deleteTask(projectId, taskId)
                    }
                    navController.popBackStack()
                },
                dialogTitle = "Delete $name",
                dialogText = """
                    Do you want to delete task $name?
                    This action is irreversible.
                """.trimIndent(),
                icon = painterResource(R.drawable.ic_delete_forever)
            )
        }
    }
}
