package io.github.first_uninteresting_username.projects_list.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import io.github.first_uninteresting_username.projects_list.R
import io.github.first_uninteresting_username.projects_list.ui.components.ActionButton
import io.github.first_uninteresting_username.projects_list.ui.components.SimpleTopBar
import io.github.first_uninteresting_username.projects_list.ui.viewmodel.ProjectViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(navController: NavHostController, viewModel: ProjectViewModel) {
    val settings by viewModel.searchSettings.collectAsState()

    var motivation by remember { mutableStateOf(settings.minMotivation..settings.maxMotivation) }
    var priority by remember { mutableStateOf(settings.minPriority..settings.maxPriority) }
    var favorite by remember { mutableStateOf(settings.showFavorite) }
    var nonFavorite by remember { mutableStateOf(settings.showNonFavorite) }
    var unfinished by remember { mutableStateOf(settings.showUnfinished) }
    var finished by remember { mutableStateOf(settings.showFinished) }

    val applyAndGoBack = {
        viewModel.updateSearchSettings(
            settings.copy(
                minMotivation = motivation.start,
                maxMotivation = motivation.endInclusive,
                minPriority = priority.start,
                maxPriority = priority.endInclusive,
                showFavorite = favorite,
                showNonFavorite = nonFavorite,
                showUnfinished = unfinished,
                showFinished = finished
            )
        )
        navController.popBackStack()
        Unit
    }

    val resetFilters = {
        motivation = 0f..10f
        priority = 0f..10f
        favorite = true
        nonFavorite = true
        unfinished = true
        finished = true
    }

    BackHandler(onBack = applyAndGoBack)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            ActionButton(
                onClick = applyAndGoBack,
                icon = painterResource(R.drawable.ic_exit_to_app),
                contentDescription = "Apply filters"
            )
        },
        topBar = {
            SimpleTopBar(navController = navController, name = "Settings", onBack = applyAndGoBack)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top)
        ) {
            FilterRange("Motivation", motivation) { motivation = it }
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 1.dp)

            FilterRange("Priority", priority) { priority = it }
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 1.dp)

            FilterToggles(
                left = "favorite" to favorite,
                right = "non favorite" to nonFavorite,
                onLeft = { favorite = !favorite },
                onRight = { nonFavorite = !nonFavorite }
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 1.dp)

            FilterToggles(
                left = "unfinished" to unfinished,
                right = "finished" to finished,
                onLeft = { unfinished = !unfinished },
                onRight = { finished = !finished }
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 1.dp)
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        resetFilters()
                    },
                    modifier = Modifier
                        .widthIn(min = 200.dp)
                        .heightIn(min = 64.dp),
                    shape = RoundedCornerShape(100.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary

                    ),
                    contentPadding = PaddingValues(horizontal = 32.dp, vertical = 20.dp),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_reset_settings),
                        contentDescription = "Reset filters",
                    )
                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "Reset filters",
                        style = MaterialTheme.typography.titleLarge,
                    )

                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterRange(
    name: String,
    range: ClosedFloatingPointRange<Float>,
    onChange: (ClosedFloatingPointRange<Float>) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        RangeSlider(
            value = range,
            onValueChange = onChange,
            valueRange = 0f..10f,
            steps = 9,
            modifier = Modifier.fillMaxWidth(0.9f)
        )
        Text(
            text = "$name ${range.start.roundToInt()} – ${range.endInclusive.roundToInt()}",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.9f)
        )
    }
}

@Composable
private fun FilterToggles(
    left: Pair<String, Boolean>,
    right: Pair<String, Boolean>,
    onLeft: () -> Unit,
    onRight: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            ToggleSwitch(left.first, left.second, onLeft)
        }
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            ToggleSwitch(right.first, right.second, onRight)
        }
    }
}

@Composable
private fun ToggleSwitch(label: String, checked: Boolean, onToggle: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label)
        Switch(checked = checked, onCheckedChange = { onToggle() })
    }
}
