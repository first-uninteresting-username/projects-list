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
import io.github.first_uninteresting_username.projects_list.ui.components.ProjectEditingColumn
import io.github.first_uninteresting_username.projects_list.ui.viewmodel.ProjectViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProjectScreen(
    navController: NavHostController,
    viewModel: ProjectViewModel,
    projectId: String?,
) {
    val projects by viewModel.projects.collectAsState()
    val project = projects.find { it.uuid == projectId }

    var priorityFloat by remember(projectId) { mutableFloatStateOf(project?.priority ?: 0f) }
    var motivationFloat by remember(projectId) { mutableFloatStateOf(project?.motivation ?: 0f) }
    var name by remember(projectId) { mutableStateOf(project?.title ?: "") }
    var description by remember(projectId) { mutableStateOf(project?.description ?: "") }
    var link by remember(projectId) { mutableStateOf(project?.link ?: "") }
    var finished by remember(projectId) { mutableStateOf(project?.finished ?: false) }
    var confirmationDialog by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var deletionDialog by remember { mutableStateOf(false) }
    var favorite by remember(projectId) { mutableStateOf(project?.favorite ?: false) }

    val saveEdits: () -> Unit = {
        project?.let {
            viewModel.updateProject(
                it.copy(
                    title = name,
                    description = description,
                    link = link,
                    priority = priorityFloat,
                    motivation = motivationFloat,
                    finished = finished,
                    favorite = favorite,
                )
            )
        }
    }

    val toggleFavourite: () -> Unit = {
        favorite = !favorite
        project?.let { viewModel.updateProject(it.copy(favorite = favorite)) }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Editing $name") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                        saveEdits()
                    }) {
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
                    saveEdits()
                    navController.popBackStack()
                },
                icon = painterResource(R.drawable.ic_exit_to_app),
                contentDescription = "Finish editing",
            )
        }
    ) { innerPadding ->
        Column(Modifier.verticalScroll(rememberScrollState())) {
            ProjectEditingColumn(
                modifier = Modifier.padding(innerPadding),
                nameValue = name,
                descriptionValue = description,
                linkValue = link,
                priorityValue = priorityFloat,
                motivationValue = motivationFloat,
                onNameValueChange = { name = it },
                onDescriptionValueChange = { description = it },
                onLinkValueChange = { link = it },
                onPriorityChange = { priorityFloat = it },
                onMotivationChange = { motivationFloat = it },
            )
            Spacer(Modifier.height(8.dp))
            ConfirmationButton(
                icon = painterResource(R.drawable.ic_done_all),
                text = "Mark as finished",
                clickedText = "Mark as unfinished",
                contentDescription = "Toggle state of completion of the project",
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
                    project?.let { viewModel.updateProject(it.copy(finished = finished)) }
                },
                dialogTitle = if (!finished) {
                    "Mark as finished"
                } else {
                    "Mark as unfinished"
                },
                dialogText = if (!finished) {
                    "Do you want to mark project $name as finished?"
                } else {
                    "Do you want to mark project $name as unfinished?"
                },
                icon = painterResource(R.drawable.ic_done_all)
            )
        }
        if (deletionDialog) {
            AlertDialogExample(
                onDismissRequest = { deletionDialog = !deletionDialog },
                onConfirmation = {
                    deletionDialog = !deletionDialog
                    projectId?.let { viewModel.deleteProject(it) }
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) {
                            inclusive = false
                        }
                    }
                },
                dialogTitle = "Delete $name",
                dialogText = """
                    Do you want to delete project $name with all tasks assigned to it?
                    This action is irreversible.
                """.trimIndent(),
                icon = painterResource(R.drawable.ic_delete_forever)
            )
        }
    }
}