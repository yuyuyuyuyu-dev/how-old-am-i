package io.github.yukoba.howoldami.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.yukoba.howoldami.ui.components.TopAppBar
import io.github.yukoba.howoldami.ui.types.NavigateDestination
import io.github.yukoba.howoldami.ui.views.mainview.MainView
import io.github.yukoba.howoldami.ui.views.thirdpartylicensesview.ThirdPartyLicensesView

@Composable
fun App(
    navHostController: NavHostController = rememberNavController(),
) {
    MaterialTheme(
        typography = typography()
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = "年齢の計算",
                    navHostController = navHostController,
                )
            },
        ) { innerPadding ->
            NavHost(
                navController = navHostController,
                startDestination = NavigateDestination.MAIN.name,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding)
            ) {
                composable(route = NavigateDestination.MAIN.name) {
                    MainView()
                }

                composable(route = NavigateDestination.THIRD_PARTY_LICENSES.name) {
                    ThirdPartyLicensesView()
                }
            }
        }
    }
}