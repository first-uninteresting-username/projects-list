package io.github.first_uninteresting_username.projects_list.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import io.github.first_uninteresting_username.projects_list.ui.components.ProjectRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrototypeScreen(navController: NavHostController) {
    Scaffold { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding))
    }
}