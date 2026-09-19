package io.github.first_uninteresting_username.projects_list.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import io.github.first_uninteresting_username.projects_list.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTopBar(
    navController: NavHostController,
    name: String,
    onBack: () -> Unit = { navController.popBackStack() }
    ) {
    TopAppBar(
        title = { Text(name) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = "Back",
                )
            }
        },
    )
}