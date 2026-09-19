package io.github.first_uninteresting_username.projects_list.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import io.github.first_uninteresting_username.projects_list.R
import java.security.KeyStore

@Composable
fun AppBottomBar(
    content: String,
    onSearchClick: () -> Unit,
    onAddClick: () -> Unit,
    onRandomClick: () -> Unit,
    modifier: Modifier = Modifier.Companion,
) {

    NavigationBar(
        modifier = modifier
    ) {
        NavigationBarItem(
            selected = false,
            onClick = {
                onSearchClick()
            },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = "Search ${content}s",
                )
            },
            label = { Text("Search ${content}s") },
        )
        NavigationBarItem(
            selected = false,
            onClick = {
                onAddClick()
            },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = "New ${content}",
                )
            },
            label = { Text("New ${content}") },
        )
        NavigationBarItem(
            // I'm a genius
            selected = false,
            onClick = {
                onRandomClick()
            },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_casino),
                    contentDescription = "Random ${content}",
                )
            },
            label = { Text("Random ${content}") },
        )
    }
}