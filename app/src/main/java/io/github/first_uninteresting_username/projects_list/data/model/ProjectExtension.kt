package io.github.first_uninteresting_username.projects_list.data.model

fun Project.withNewTask(task: Task): Project =
    copy(tasks = tasks + task)

fun Project.withUpdatedTask(task: Task): Project =
    copy(tasks = tasks.map { if (it.uuid == task.uuid) task else it })

fun Project.withoutTask(taskId: String): Project =
    copy(tasks = tasks.filterNot { it.uuid == taskId })