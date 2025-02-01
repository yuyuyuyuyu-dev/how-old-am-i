package io.github.yukoba.howoldami.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontFamily
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import how_old_am_i.composeapp.generated.resources.Res
import how_old_am_i.composeapp.generated.resources.Yomogi_Regular
import io.github.yukoba.howoldami.ui.components.TopAppBar
import io.github.yukoba.howoldami.ui.howoldami.HowOldAmIViewModel
import io.github.yukoba.howoldami.ui.howoldami.screens.HowOldAmIScreen
import io.github.yukoba.howoldami.ui.thirdpartylicenses.ThirdPartyLicensesViewModel
import io.github.yukoba.howoldami.ui.thirdpartylicenses.screens.ThirdPartyLicensesScreen
import org.jetbrains.compose.resources.Font

@Composable
fun App(
    howOldAmIViewModel: HowOldAmIViewModel = viewModel { HowOldAmIViewModel() },
    thirdPartyLicensesViewModel: ThirdPartyLicensesViewModel = viewModel { ThirdPartyLicensesViewModel() },
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
        val thirdPartyLicensesUiState by thirdPartyLicensesViewModel.uiState.collectAsState()

        val uriHandler = LocalUriHandler.current

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = "年齢の計算",
                    navigateBackIsPossible = navController.previousBackStackEntry != null,
                    onNavigateBackButtonClick = {
                        navController.navigateUp()
                    },
                    onNavigateThirdPartyLicensesButtonClick = {
                        if (currentScreen != NavigateDestination.ThirdPartyLicenses) {
                            navController.navigate(NavigateDestination.ThirdPartyLicenses.name)
                        }
                    },
                )
            },
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = NavigateDestination.HowOldAmI.name,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
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
                    ThirdPartyLicensesScreen(
                        thirdPartyLicenses = thirdPartyLicensesUiState.thirdPartyLicenses,
                        uriHandler = uriHandler,
                    )
                }
            }
        }
    }
}
