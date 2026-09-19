package io.github.first_uninteresting_username.projects_list.data.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Task(
    val uuid: String = UUID.randomUUID().toString(),
    val chronology: Int,
    val title: String,
    val description: String = "",
    val priority: Float,
    val motivation: Float,
    val finished: Boolean = false,
    val favorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
) {
    val score: Int
        get() = motivation.toInt() * priority.toInt()
    init {
        require(priority in 0f..10f) { "priority must be 0–10, got $priority" }
        require(motivation in 0f..10f) { "motivation must be 0–10, got $motivation" }
    }
}