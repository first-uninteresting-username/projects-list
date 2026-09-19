package io.github.first_uninteresting_username.projects_list.data.model

enum class SortOption(val label: String) {
    CHRONOLOGY("Chronology"),
    TITLE("Title"),
    PRIORITY("Priority"),
    MOTIVATION("Motivation"),
    SCORE("Score"),
    MATCH_QUERY("Match query"),
}

data class SearchSettings(
    val minPriority: Float = 0f,
    val maxPriority: Float = 10f,
    val minMotivation: Float = 0f,
    val maxMotivation: Float = 10f,
    val showFavorite: Boolean = true,
    val showNonFavorite: Boolean = true,
    val showUnfinished: Boolean = true,
    val showFinished: Boolean = true,
    val sortBy: SortOption = SortOption.MATCH_QUERY,
    val descending: Boolean = false,
)

private fun Project.queryMatchRank(query: String): Int {
    val inTitle = title.indexOf(query, ignoreCase = true)
    if (inTitle >= 0) return inTitle
    val inDescription = description.indexOf(query, ignoreCase = true)
    if (inDescription >= 0) return title.length + inDescription
    return Int.MAX_VALUE
}

fun List<Project>.filterAndSort(query: String, settings: SearchSettings): List<Project> {
    val filtered = filter { project ->
        (project.title.contains(query, ignoreCase = true) ||
                project.description.contains(query, ignoreCase = true)) &&
                project.priority in settings.minPriority..settings.maxPriority &&
                project.motivation in settings.minMotivation..settings.maxMotivation &&
                (if (project.favorite) settings.showFavorite else settings.showNonFavorite) &&
                (if (project.finished) settings.showFinished else settings.showUnfinished)
    }
    val sorted = when (settings.sortBy) {
        SortOption.CHRONOLOGY -> filtered.sortedWith(compareBy({ it.chronology }, { it.createdAt }))
        SortOption.TITLE -> filtered.sortedBy { it.title.lowercase() }
        SortOption.PRIORITY -> filtered.sortedBy { it.priority }
        SortOption.MOTIVATION -> filtered.sortedBy { it.motivation }
        SortOption.SCORE -> filtered.sortedBy { it.score }
        SortOption.MATCH_QUERY -> filtered.sortedWith(compareBy({ it.queryMatchRank(query) }, { it.chronology }))
    }
    return if (settings.descending) sorted.reversed() else sorted
}

private fun Task.queryMatchRank(query: String): Int {
    val inTitle = title.indexOf(query, ignoreCase = true)
    if (inTitle >= 0) return inTitle
    val inDescription = description.indexOf(query, ignoreCase = true)
    if (inDescription >= 0) return title.length + inDescription
    return Int.MAX_VALUE
}

@JvmName("filterAndSortTasks")
fun List<Task>.filterAndSort(query: String, settings: SearchSettings): List<Task> {
    val filtered = filter { task ->
        (task.title.contains(query, ignoreCase = true) ||
                task.description.contains(query, ignoreCase = true)) &&
                task.priority in settings.minPriority..settings.maxPriority &&
                task.motivation in settings.minMotivation..settings.maxMotivation &&
                (if (task.favorite) settings.showFavorite else settings.showNonFavorite) &&
                (if (task.finished) settings.showFinished else settings.showUnfinished)
    }
    val sorted = when (settings.sortBy) {
        SortOption.CHRONOLOGY -> filtered.sortedWith(compareBy({ it.chronology }, { it.createdAt }))
        SortOption.TITLE -> filtered.sortedBy { it.title.lowercase() }
        SortOption.PRIORITY -> filtered.sortedBy { it.priority }
        SortOption.MOTIVATION -> filtered.sortedBy { it.motivation }
        SortOption.SCORE -> filtered.sortedBy { it.score }
        SortOption.MATCH_QUERY -> filtered.sortedWith(compareBy({ it.queryMatchRank(query) }, { it.chronology }))
    }
    return if (settings.descending) sorted.reversed() else sorted
}
