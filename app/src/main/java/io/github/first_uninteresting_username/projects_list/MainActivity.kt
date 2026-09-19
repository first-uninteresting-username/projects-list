package io.github.first_uninteresting_username.projects_list

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.github.first_uninteresting_username.projects_list.ui.screens.AboutScreen
import io.github.first_uninteresting_username.projects_list.ui.screens.EditProjectScreen
import io.github.first_uninteresting_username.projects_list.ui.screens.EditTaskScreen
import io.github.first_uninteresting_username.projects_list.ui.screens.FilterScreen
import io.github.first_uninteresting_username.projects_list.ui.screens.HomeScreen
import io.github.first_uninteresting_username.projects_list.ui.screens.LicensesScreen
import io.github.first_uninteresting_username.projects_list.ui.screens.NewProjectScreen
import io.github.first_uninteresting_username.projects_list.ui.screens.NewTaskScreen
import io.github.first_uninteresting_username.projects_list.ui.screens.ProjectScreen
import io.github.first_uninteresting_username.projects_list.ui.screens.PrototypeScreen
import io.github.first_uninteresting_username.projects_list.ui.screens.SearchScreen
import io.github.first_uninteresting_username.projects_list.ui.screens.SettingsScreen
import io.github.first_uninteresting_username.projects_list.ui.screens.SortScreen
import io.github.first_uninteresting_username.projects_list.ui.screens.TaskFilterScreen
import io.github.first_uninteresting_username.projects_list.ui.screens.TaskScreen
import io.github.first_uninteresting_username.projects_list.ui.screens.TaskSearchScreen
import io.github.first_uninteresting_username.projects_list.ui.screens.TaskSortScreen
import io.github.first_uninteresting_username.projects_list.ui.theme.ProjectsTheme
import io.github.first_uninteresting_username.projects_list.ui.viewmodel.ProjectViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjectsTheme {
                AppNavigation()
            }
        }
    }

    @Composable
    fun AppNavigation() {
        val navController = rememberNavController()
        val viewModel: ProjectViewModel = viewModel(factory = ProjectViewModel.Factory)

        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
        ) {
            composable(Routes.PROTOTYPE) {
                PrototypeScreen(navController)
            }
            composable(Routes.HOME) {
                HomeScreen(navController, viewModel)
            }
            composable(Routes.SETTINGS) {
                SettingsScreen(navController, viewModel)
            }
            composable(Routes.ABOUT) {
                AboutScreen(navController)
            }
            composable(Routes.LICENSES) {
                LicensesScreen(navController)
            }
            composable(Routes.NEW_PROJECT) {
                NewProjectScreen(navController, viewModel)
            }
            composable(
                route = Routes.PROJECT_ROUTE,
                arguments = listOf(navArgument(Routes.PROJECT_ID_ARG) { type = NavType.StringType })
            ) { backStackEntry ->
                ProjectScreen(
                    navController = navController,
                    viewModel = viewModel,
                    projectId = backStackEntry.arguments?.getString(Routes.PROJECT_ID_ARG),
                )
            }
            composable(
                route = Routes.EDIT_PROJECT_ROUTE,
                arguments = listOf(navArgument(Routes.PROJECT_ID_ARG) { type = NavType.StringType })
            ) { backStackEntry ->
                EditProjectScreen(
                    navController = navController,
                    viewModel = viewModel,
                    projectId = backStackEntry.arguments?.getString(Routes.PROJECT_ID_ARG),
                )
            }
            composable(
                route = Routes.NEW_TASK_ROUTE,
                arguments = listOf(navArgument(Routes.PROJECT_ID_ARG) { type = NavType.StringType })
            ) { backStackEntry ->
                NewTaskScreen(
                    navController = navController,
                    viewModel = viewModel,
                    projectId = backStackEntry.arguments?.getString(Routes.PROJECT_ID_ARG),
                )
            }
            composable(
                route = Routes.TASK_ROUTE,
                arguments = listOf(
                    navArgument(Routes.PROJECT_ID_ARG) { type = NavType.StringType },
                    navArgument(Routes.TASK_ID_ARG) { type = NavType.StringType }
                )
            ) { backStackEntry ->
                TaskScreen(
                    navController = navController,
                    viewModel = viewModel,
                    projectId = backStackEntry.arguments?.getString(Routes.PROJECT_ID_ARG),
                    taskId = backStackEntry.arguments?.getString(Routes.TASK_ID_ARG),
                )
            }
            composable(
                route = Routes.EDIT_TASK_ROUTE,
                arguments = listOf(
                    navArgument(Routes.PROJECT_ID_ARG) { type = NavType.StringType },
                    navArgument(Routes.TASK_ID_ARG) { type = NavType.StringType }
                )
            ) { backStackEntry ->
                EditTaskScreen(
                    navController = navController,
                    viewModel = viewModel,
                    projectId = backStackEntry.arguments?.getString(Routes.PROJECT_ID_ARG),
                    taskId = backStackEntry.arguments?.getString(Routes.TASK_ID_ARG),
                )
            }
            composable(Routes.SEARCH) {
                SearchScreen(
                    navController = navController,
                    viewModel = viewModel,
                )
            }
            composable(Routes.FILTER) {
                FilterScreen(navController, viewModel)
            }
            composable(Routes.SORT) {
                SortScreen(navController, viewModel)
            }
            composable(
                route = Routes.TASK_SEARCH_ROUTE,
                arguments = listOf(navArgument(Routes.PROJECT_ID_ARG) { type = NavType.StringType })
            ) { backStackEntry ->
                TaskSearchScreen(
                    navController = navController,
                    viewModel = viewModel,
                    projectId = backStackEntry.arguments?.getString(Routes.PROJECT_ID_ARG),
                )
            }
            composable(Routes.TASK_FILTER) {
                TaskFilterScreen(navController, viewModel)
            }
            composable(Routes.TASK_SORT) {
                TaskSortScreen(navController, viewModel)
            }
        }
    }

}