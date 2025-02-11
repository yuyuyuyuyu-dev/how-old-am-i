package dev.yuyuyuyuyu.howoldami.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    title: String,
    navigateBackIsPossible: Boolean,
    openSourceLicensesButtonLabel: @Composable () -> Unit = { Text("オープンソース ライセンス") },
    sourceCodeButtonLabel: @Composable () -> Unit = { Text("ソースコード") },
    onNavigateBackButtonClick: () -> Unit,
    onOpenSourceLicensesButtonClick: () -> Unit,
    onSourceCodeButtonClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = { Text(title) },
        modifier = modifier,
        navigationIcon = {
            if (navigateBackIsPossible) {
                IconButton(
                    content = { Icon(Icons.AutoMirrored.Filled.ArrowBack, "back") },
                    onClick = onNavigateBackButtonClick,
                )
            }
        },
        actions = {
            var menuIsExpanded by rememberSaveable { mutableStateOf(false) }

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
                    text = openSourceLicensesButtonLabel,
                    onClick = {
                        onOpenSourceLicensesButtonClick()
                        menuIsExpanded = false
                    },
                )
                if (onSourceCodeButtonClick != null) {
                    DropdownMenuItem(
                        text = sourceCodeButtonLabel,
                        onClick = {
                            onSourceCodeButtonClick()
                            menuIsExpanded = false
                        },
                    )
                }
            }
        },
    )
}
