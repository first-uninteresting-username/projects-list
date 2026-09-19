package io.github.first_uninteresting_username.projects_list.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import io.github.first_uninteresting_username.projects_list.ui.components.AppBottomBar
import io.github.first_uninteresting_username.projects_list.ui.components.MinimalDialog
import io.github.first_uninteresting_username.projects_list.ui.components.ProjectRow
import io.github.first_uninteresting_username.projects_list.ui.viewmodel.ProjectViewModel
import kotlin.random.Random


@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: ProjectViewModel
) {
    val projects by viewModel.projects.collectAsState()
    val visibleProjects = projects
        .filterNot { it.finished }
        .sortedWith(compareBy<Project> { it.chronology }.thenBy { it.createdAt })
    var noProjectsDialog by remember { mutableStateOf(false) }
    val randomProject = randomWeightedProject(projects)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppTopBar(
                onSettingsClick = { navController.navigate(Routes.SETTINGS) },
                onAboutClick = { navController.navigate(Routes.ABOUT) },
            )
        },
        bottomBar = {
            AppBottomBar(
                content = "project",
                onSearchClick = {
                    navController.navigate("search")
                },
                onAddClick = { navController.navigate("new_project") },
                onRandomClick = {
                    if (randomProject == null) {
                        noProjectsDialog = !noProjectsDialog
                    } else {
                        navController.navigate(Routes.projectRoute(randomProject.uuid))
                    }
                },
            )
        }
    ) { innerPadding ->
        if (visibleProjects.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("No projects yet! Tap the + icon below to add one.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(visibleProjects) { project ->
                    ProjectRow(
                        name = project.title,
                        isFavorite = project.favorite,
                        priority = project.priority,
                        motivation = project.motivation,
                        chronology = project.chronology,
                        onClick = {
                            navController.navigate(Routes.projectRoute(project.uuid))
                        }
                    )
                }
            }
        }
        if (noProjectsDialog) {
            MinimalDialog(
                onDismissRequest = { noProjectsDialog = !noProjectsDialog },
                text = "No projects were found",
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    onSettingsClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text("Home") },
        modifier = Modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        actions = {
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
                    text = { Text("Settings") },
                    onClick = {
                        expanded = false
                        onSettingsClick()
                    }
                )
                DropdownMenuItem(
                    text = { Text("About") },
                    onClick = {
                        expanded = false
                        onAboutClick()
                    }
                )
            }
        }
    )
}

fun randomWeightedProject(projects: List<Project>): Project? {
    val unfinished = projects.filterNot { it.finished }
    var totalScore = 0
    for (project in unfinished) {
        totalScore += project.score
    }

    if (totalScore == 0) {
        if (unfinished.isEmpty()) {
            return projects.randomOrNull()
        }
        return unfinished.randomOrNull()
    }

    var roll = Random.nextDouble(totalScore.toDouble())
    for (project in unfinished) {
        roll -= project.score
        if (roll < 0) return project
    }

    return unfinished.randomOrNull()
}
