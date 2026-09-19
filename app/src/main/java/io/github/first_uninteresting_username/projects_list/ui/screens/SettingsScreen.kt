package io.github.first_uninteresting_username.projects_list.ui.screens

import android.text.format.Formatter
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import io.github.first_uninteresting_username.projects_list.R
import io.github.first_uninteresting_username.projects_list.data.model.Project
import io.github.first_uninteresting_username.projects_list.ui.components.ConfirmationButton
import io.github.first_uninteresting_username.projects_list.ui.components.SimpleProjectRow
import io.github.first_uninteresting_username.projects_list.ui.components.SimpleTopBar
import io.github.first_uninteresting_username.projects_list.ui.viewmodel.ProjectViewModel
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController, viewModel: ProjectViewModel) {
    val projects by viewModel.projects.collectAsState()

    var deleteAllDialog by remember { mutableStateOf(false) }
    var deleteFinishedDialog by remember { mutableStateOf(false) }

    val storageSize by viewModel.storageSize.collectAsState()
    val context = LocalContext.current

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        uri?.let {
            context.contentResolver.openOutputStream(it)?.use { outputStream ->
                OutputStreamWriter(outputStream).use { writer ->
                    writer.write(viewModel.exportData())
                }
            }
        }
    }

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            context.contentResolver.openInputStream(it)?.use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    val jsonString = reader.readText()
                    viewModel.importData(jsonString, replace = false)
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SimpleTopBar(
                navController = navController,
                name = "Settings"
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(4.dp),
        ) {
            // SectionHeader(text = "Data")
            ActionRow(
                icon = painterResource(R.drawable.ic_upload),
                title = "Export data",
                subtitle = "Export all projects and tasks",
                enabled = true,
                onClick = {
                    exportLauncher.launch("projects_backup.json")
                }
            )
            Spacer(Modifier.height(8.dp))
            ActionRow(
                icon = painterResource(R.drawable.ic_download),
                title = "Import data",
                subtitle = "Import data from a backup",
                enabled = true,
                onClick = {
                    importLauncher.launch(arrayOf("application/json"))
                }
            )
            Spacer(Modifier.height(8.dp))
            ActionRow(
                icon = painterResource(R.drawable.ic_delete),
                title = "Prune finished",
                subtitle = "Delete all finished projects and tasks",
                enabled = true,
                onClick = { deleteFinishedDialog = !deleteFinishedDialog }
            )
            Spacer(Modifier.height(8.dp))
            ActionRow(
                icon = painterResource(R.drawable.ic_delete_forever),
                title = "Delete all data",
                subtitle = "Delete all projects and tasks",
                enabled = true,
                onClick = { deleteAllDialog = !deleteAllDialog }
            )
            Spacer(Modifier.height(8.dp))
            ActionRow(
                icon = painterResource(R.drawable.ic_storage),
                title = "Used storage: ${Formatter.formatShortFileSize(context, storageSize)}",
                subtitle = "Something went very wrong if it's above 10MB",
                enabled = true,
                onClick = { }
            )
            if (deleteAllDialog) {
                DeletionDialog(
                    viewModel = viewModel,
                    finishedOnly = false,
                    projectsList = projects,
                    onDismissRequest = { deleteAllDialog = !deleteAllDialog }
                )
            }
            if (deleteFinishedDialog) {
                DeletionDialog(
                    viewModel = viewModel,
                    finishedOnly = true,
                    projectsList = projects,
                    onDismissRequest = { deleteFinishedDialog = !deleteFinishedDialog }
                )
            }
        }
    }
}

@Composable
fun DeletionDialog(
    viewModel: ProjectViewModel,
    finishedOnly: Boolean = true,
    projectsList: List<Project>,
    onDismissRequest: () -> Unit
) {
    val projects = projectsList
        .filter { if (finishedOnly) it.finished else true }
        .sortedWith(compareBy<Project> { it.chronology }.thenBy { it.createdAt })

    val tasks = buildList {
        for (project in projectsList) {
            for (task in project.tasks) {
                if (task.finished || !finishedOnly) {
                    add(task to project)
                }
            }
        }
    }
    Dialog(onDismissRequest = onDismissRequest) {
        Card(shape = RoundedCornerShape(16.dp)) {
            Column(Modifier.padding(24.dp)) {
                Text("Items to be deleted", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(24.dp))
                LazyColumn(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(projects) { project ->
                        SimpleProjectRow(
                            name = project.title,
                            chronology = project.chronology,
                        )
                    }
                    items(tasks) { (task, project) ->
                        SimpleProjectRow(
                            name = task.title,
                            chronology = project.chronology,
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                ConfirmationButton(
                    onClick = {
                        onDismissRequest()
                        for (project in projects) {
                            viewModel.deleteProject(project.uuid)
                        }
                        tasks.forEach { (task, project) ->
                            viewModel.deleteTask(
                                projectUuid = project.uuid,
                                taskUuid = task.uuid
                            )
                        }
                    },
                    text = "Confirm deletion"
                )
            }
        }
    }
}