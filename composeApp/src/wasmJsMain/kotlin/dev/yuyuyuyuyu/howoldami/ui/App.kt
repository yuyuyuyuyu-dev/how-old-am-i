package dev.yuyuyuyuyu.howoldami.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.yuyuyuyuyu.howoldami.ui.howoldami.HowOldAmIViewModel
import dev.yuyuyuyuyu.howoldami.ui.howoldami.screens.HowOldAmIScreen
import dev.yuyuyuyuyu.howoldami.ui.opensourcelicenses.screens.OpenSourceLicensesScreen
import dev.yuyuyuyuyu.simpletopappbar.SimpleTopAppBar
import how_old_am_i.composeapp.generated.resources.Res
import how_old_am_i.composeapp.generated.resources.Yomogi_Regular
import org.jetbrains.compose.resources.Font

@Composable
fun App(
    howOldAmIViewModel: HowOldAmIViewModel = viewModel { HowOldAmIViewModel() },
    navController: NavHostController = rememberNavController(),
) {
    MaterialTheme(
        typography = createTypography(fontFamily = FontFamily(Font(Res.font.Yomogi_Regular))),
    ) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentScreen = NavigateDestination.valueOf(
            backStackEntry?.destination?.route ?: NavigateDestination.HowOldAmI.name
        )

        val howOldAmIUiState by howOldAmIViewModel.uiState.collectAsState()

        val uriHandler = LocalUriHandler.current

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                SimpleTopAppBar(
                    title = "年齢の計算",
                    navigateBackIsPossible = navController.previousBackStackEntry != null,
                    sourceCodeButtonLabel = {
                        Row {
                            Icon(Icons.AutoMirrored.Filled.OpenInNew, "open in new icon")
                            Spacer(Modifier.width(1.dp))
                            Text("ソースコード")
                        }
                    },
                    onNavigateBackButtonClick = {
                        navController.navigateUp()
                    },
                    onOpenSourceLicensesButtonClick = {
                        if (currentScreen != NavigateDestination.ThirdPartyLicenses) {
                            navController.navigate(NavigateDestination.ThirdPartyLicenses.name)
                        }
                    },
                    onSourceCodeButtonClick = {
                        uriHandler.openUri("https://github.com/yu-ko-ba/how-old-am-i")
                    },
                )
            },
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = NavigateDestination.HowOldAmI.name,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                composable(route = NavigateDestination.HowOldAmI.name) {
                    HowOldAmIScreen(
                        dateOfBirth = howOldAmIUiState.dateOfBirth,
                        age = howOldAmIUiState.age,
                        onDateOfBirthChange = howOldAmIViewModel::onDateOfBirthChange,
                    )
                }

                composable(route = NavigateDestination.ThirdPartyLicenses.name) {
                    OpenSourceLicensesScreen()
                }
            }
        }
    }
}
