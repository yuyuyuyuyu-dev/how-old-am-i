package dev.yuyuyuyuyu.howoldami.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.unit.dp


@Composable
fun TopAppBar(
    title: String,
    navigateBackIsPossible: Boolean,
    sourceCodeURL: String,
    uriHandler: UriHandler,
    onNavigateBackButtonClick: () -> Unit,
    onNavigateThirdPartyLicensesButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = title,
        navigateSourceCodeButtonLabel = {
            Row {
                Icon(Icons.AutoMirrored.Filled.OpenInNew, "open in new icon")
                Spacer(Modifier.width(1.dp))
                Text("ソースコード")
            }
        },
        navigateBackIsPossible = navigateBackIsPossible,
        onNavigateBackButtonClick = onNavigateBackButtonClick,
        onNavigateThirdPartyLicensesButtonClick = onNavigateThirdPartyLicensesButtonClick,
        onNavigateSourceCodeButtonClick = {
            uriHandler.openUri(sourceCodeURL)
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    title: String,
    navigateBackIsPossible: Boolean,
    onNavigateBackButtonClick: () -> Unit,
    onNavigateThirdPartyLicensesButtonClick: () -> Unit,
    onNavigateSourceCodeButtonClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = title,
        navigateSourceCodeButtonLabel = { Text("ソースコード") },
        navigateBackIsPossible = navigateBackIsPossible,
        onNavigateBackButtonClick = onNavigateBackButtonClick,
        onNavigateThirdPartyLicensesButtonClick = onNavigateThirdPartyLicensesButtonClick,
        onNavigateSourceCodeButtonClick = onNavigateSourceCodeButtonClick,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBar(
    title: String,
    navigateSourceCodeButtonLabel: @Composable () -> Unit,
    navigateBackIsPossible: Boolean,
    onNavigateBackButtonClick: () -> Unit,
    onNavigateThirdPartyLicensesButtonClick: () -> Unit,
    onNavigateSourceCodeButtonClick: (() -> Unit)? = null,
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
                    text = { Text("サードパーティーライセンス") },
                    onClick = {
                        onNavigateThirdPartyLicensesButtonClick()
                        menuIsExpanded = false
                    },
                )
                if (onNavigateSourceCodeButtonClick != null) {
                    DropdownMenuItem(
                        text = navigateSourceCodeButtonLabel,
                        onClick = {
                            onNavigateSourceCodeButtonClick()
                            menuIsExpanded = false
                        },
                    )
                }
            }
        },
    )
}
