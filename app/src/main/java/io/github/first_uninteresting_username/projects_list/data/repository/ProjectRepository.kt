package io.github.first_uninteresting_username.projects_list.data.repository

import android.content.Context
import io.github.first_uninteresting_username.projects_list.data.model.Project
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import java.io.File


class ProjectRepository(context: Context) {

    private val file = File(context.filesDir, "projects.json")
    private val json = Json { prettyPrint = true; ignoreUnknownKeys = true }

    private val _projects = MutableStateFlow<List<Project>>(emptyList())
    val projects: StateFlow<List<Project>> = _projects.asStateFlow()

    private val _storageSize = MutableStateFlow(0L)
    val storageSize: StateFlow<Long> = _storageSize.asStateFlow()

    init {
        load()
    }

    private fun load() {
        _storageSize.value = file.length()
        _projects.value = if (file.exists()) {
            runCatching { json.decodeFromString<List<Project>>(file.readText()) }
                .getOrDefault(emptyList())
        } else emptyList()
    }

    private fun persist() {
        file.writeText(json.encodeToString(_projects.value))
    }

    fun addProject(project: Project) {
        _projects.value = _projects.value + project
        persist()
    }

    fun updateProject(updated: Project) {
        _projects.value = _projects.value.map { if (it.uuid == updated.uuid) updated else it }
        persist()
    }

    fun deleteProject(uuid: String) {
        _projects.value = _projects.value.filterNot { it.uuid == uuid }
        persist()
    }

    fun exportJson(): String = json.encodeToString(_projects.value)

    fun importJson(jsonString: String, replace: Boolean = false) {
        val imported = json.decodeFromString<List<Project>>(jsonString)
        _projects.value = if (replace) {
            imported
        } else {
            val merged = _projects.value.associateBy { it.uuid }.toMutableMap()
            imported.forEach { merged[it.uuid] = it }
            merged.values.toList()
        }
        persist()
    }
}