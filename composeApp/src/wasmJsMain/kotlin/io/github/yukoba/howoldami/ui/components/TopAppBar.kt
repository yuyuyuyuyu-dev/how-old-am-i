package io.github.yukoba.howoldami.ui.components

import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import io.github.yukoba.howoldami.ui.types.NavigateDestination

@Composable
fun TopAppBar(
    title: String,
    navHostController: NavHostController,
) {
    var canNavigateBack by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(
                    content = { Icon(Icons.Default.ArrowBack, "back") },
                    onClick = {
                        navHostController.navigate(NavigateDestination.MAIN.name)
                        canNavigateBack = false
                    },
                )
            }
        },
        actions = {
            var menuIsExpanded by remember { mutableStateOf(false) }

            IconButton(
                content = { Icon(Icons.Default.MoreVert, "menu") },
                onClick = {
                    menuIsExpanded = true
                },
            )

            DropdownMenu(
                expanded = menuIsExpanded,
                onDismissRequest = { menuIsExpanded = false },
            ) {
                DropdownMenuItem(
                    content = { Text("サードパーティーライセンス") },
                    onClick = {
                        navHostController.navigate(NavigateDestination.THIRD_PARTY_LICENSES.name)
                        menuIsExpanded = false
                        canNavigateBack = true
                    },
                )
            }
        },
    )
}