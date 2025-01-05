package io.github.yukoba.howoldami.ui.views.mainview

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import io.github.yukoba.howoldami.ui.components.MainScreen
import io.github.yukoba.howoldami.ui.components.TopAppBar
import io.github.yukoba.howoldami.usecase.calculateAgeUseCase

@Composable
fun MainView() {
    var year by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var day by remember { mutableStateOf("") }

    var age by remember { mutableStateOf("") }

    fun calculateAge() {
        calculateAgeUseCase(
            year = year,
            month = month,
            day = day,
            onSucceeded = {
                age = it
            },
            onFailed = {
                age = ""
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = "年齢の計算",
            )
        },
        content = { innerPadding ->
            MainScreen(
                year = year,
                month = month,
                day = day,
                age = age,
                onYearChanged = {
                    year = it
                    calculateAge()
                },
                onMonthChanged = {
                    month = it
                    calculateAge()
                },
                onDayChanged = {
                    day = it
                    calculateAge()
                },
                modifier = Modifier.padding(innerPadding),
            )
        },
    )
}