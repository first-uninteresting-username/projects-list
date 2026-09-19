package io.github.first_uninteresting_username.projects_list.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.github.first_uninteresting_username.projects_list.ProjectsApplication
import io.github.first_uninteresting_username.projects_list.data.model.Project
import io.github.first_uninteresting_username.projects_list.data.model.SearchSettings
import io.github.first_uninteresting_username.projects_list.data.model.Task
import io.github.first_uninteresting_username.projects_list.data.model.withNewTask
import io.github.first_uninteresting_username.projects_list.data.model.withUpdatedTask
import io.github.first_uninteresting_username.projects_list.data.model.withoutTask
import io.github.first_uninteresting_username.projects_list.data.repository.ProjectRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProjectViewModel(private val repository: ProjectRepository) : ViewModel() {

    val projects: StateFlow<List<Project>> = repository.projects

    private val _searchSettings = MutableStateFlow(SearchSettings())
    val searchSettings: StateFlow<SearchSettings> = _searchSettings

    fun updateSearchSettings(settings: SearchSettings) {
        _searchSettings.value = settings
    }

    private val _taskSearchSettings = MutableStateFlow(SearchSettings())
    val taskSearchSettings: StateFlow<SearchSettings> = _taskSearchSettings

    fun updateTaskSearchSettings(settings: SearchSettings) {
        _taskSearchSettings.value = settings
    }

    val storageSize: StateFlow<Long> = repository.storageSize

    fun addProject(
        title: String,
        description: String = "",
        link: String,
        priority: Float,
        motivation: Float,
        favorite: Boolean = false
    ) {
        val nextChronology = (projects.value.maxOfOrNull { it.chronology } ?: 0) + 1
        repository.addProject(
            Project(
                chronology = nextChronology,
                title = title,
                description = description,
                link = link,
                priority = priority,
                motivation = motivation,
                favorite = favorite
            )
        )
    }

    fun updateProject(updated: Project) = repository.updateProject(updated)

    fun deleteProject(uuid: String) = repository.deleteProject(uuid)

    fun addTask(projectUuid: String, task: Task) {
        findProject(projectUuid)?.let { repository.updateProject(it.withNewTask(task)) }
    }

    fun updateTask(projectUuid: String, task: Task) {
        findProject(projectUuid)?.let { repository.updateProject(it.withUpdatedTask(task)) }
    }

    fun deleteTask(projectUuid: String, taskUuid: String) {
        findProject(projectUuid)?.let { repository.updateProject(it.withoutTask(taskUuid)) }
    }

    fun exportData(): String = repository.exportJson()

    fun importData(jsonString: String, replace: Boolean) = repository.importJson(
        jsonString = jsonString,
        replace = replace
    )

    private fun findProject(id: String) = projects.value.find { it.uuid == id }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as ProjectsApplication
                ProjectViewModel(ProjectRepository(application))
            }
        }
    }
}
