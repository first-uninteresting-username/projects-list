package io.github.first_uninteresting_username.projects_list.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import io.github.first_uninteresting_username.projects_list.R
import io.github.first_uninteresting_username.projects_list.Routes
import io.github.first_uninteresting_username.projects_list.data.model.Project
import io.github.first_uninteresting_username.projects_list.data.model.Task
import io.github.first_uninteresting_username.projects_list.ui.components.ActionButton
import io.github.first_uninteresting_username.projects_list.ui.components.AlertDialogExample
import io.github.first_uninteresting_username.projects_list.ui.components.AppBottomBar
import io.github.first_uninteresting_username.projects_list.ui.components.ConfirmationButton
import io.github.first_uninteresting_username.projects_list.ui.components.MinimalDialog
import io.github.first_uninteresting_username.projects_list.ui.components.ProjectDisplayColumn
import io.github.first_uninteresting_username.projects_list.ui.components.ProjectRow
import io.github.first_uninteresting_username.projects_list.ui.viewmodel.ProjectViewModel
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectScreen(
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

    val toggleFavourite: () -> Unit = {
        favorite = !favorite
        project?.let { viewModel.updateProject(it.copy(favorite = favorite)) }
    }

    val tasks = project?.tasks ?: emptyList()

    val visibleTasks = tasks
        .filterNot { it.finished }
        .sortedWith(compareBy<Task> { it.chronology }.thenBy { it.createdAt })

    val randomTask = project?.let { randomWeightedTask(it) }
    var noTasksDialog by remember { mutableStateOf(false) }



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
        bottomBar = {
            AppBottomBar(
                content = "tasks",
                onSearchClick = {
                    projectId?.let { navController.navigate(Routes.taskSearchRoute(it)) }
                },
                onAddClick = {
                    projectId?.let { navController.navigate(Routes.newTaskRoute(it)) }
                },
                onRandomClick = {
                    if (randomTask == null || project == null) {
                        noTasksDialog = !noTasksDialog
                    } else {
                        navController.navigate(Routes.taskRoute(
                            projectId = project.uuid,
                            taskId = randomTask.uuid
                        ))
                    }
                }
            )
        },
        floatingActionButton = {
            ActionButton(
                onClick = {
                    project?.let { navController.navigate(Routes.editProjectRoute(it.uuid)) }
                },
                icon = painterResource(R.drawable.ic_edit),
                contentDescription = "Edit the project",
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                ProjectDisplayColumn(
                    modifier = Modifier,
                    nameValue = name,
                    descriptionValue = description,
                    linkValue = link,
                    priorityValue = priorityFloat,
                    motivationValue = motivationFloat,
                )
            }
            item {
                ConfirmationButton(
                    icon = painterResource(R.drawable.ic_done_all),
                    text = "Mark as finished",
                    clickedText = "Mark as unfinished",
                    contentDescription = "Toggle state of completion of the project",
                    onClick = { confirmationDialog = !confirmationDialog },
                    clicked = finished
                )
            }
            item {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 1.dp)
            }
            if (visibleTasks.isEmpty()) {
                item {
                    Text("No tasks yet! Tap the + icon below to add one.")
                }
            } else {
                items(visibleTasks) { task ->
                    ProjectRow(
                        name = task.title,
                        isFavorite = task.favorite,
                        priority = task.priority,
                        motivation = task.motivation,
                        chronology = task.chronology,
                        onClick = {
                            if (project != null) {
                                navController.navigate(Routes.taskRoute(project.uuid, task.uuid))
                            }
                        }
                    )
                }
            }
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
        if (noTasksDialog) {
            MinimalDialog(
                onDismissRequest = { noTasksDialog = !noTasksDialog },
                text = "No tasks were found",
            )
        }
    }
}

fun randomWeightedTask(project: Project): Task? {
    val unfinished = project.tasks.filterNot { it.finished }
    var totalScore = 0
    for (project in unfinished) {
        totalScore += project.score
    }

    if (totalScore == 0) {
        if (unfinished.isEmpty()) {
            return project.tasks.randomOrNull()
        }
        return unfinished.randomOrNull()
    }

    var roll = Random.nextDouble(totalScore.toDouble())
    for (task in unfinished) {
        roll -= task.score
        if (roll < 0) return task
    }

    return unfinished.randomOrNull()
}