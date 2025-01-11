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
import io.github.yukoba.howoldami.ui.components.types.DateOfBirth
import io.github.yukoba.howoldami.usecase.ValidateIntegerUseCase
import io.github.yukoba.howoldami.usecase.calculateAgeUseCase

@Composable
fun MainView() {
    val validateIntegerUseCase = ValidateIntegerUseCase()

    var dateOfBirth by remember { mutableStateOf(DateOfBirth()) }
    var age by remember { mutableStateOf("") }

    fun onDateOfBirthChange(date: DateOfBirth) {
        dateOfBirth = date

        calculateAgeUseCase(
            dateOfBirth = dateOfBirth,
            onSuccess = {
                age = it
            },
            onFailure = {
                age = ""
                println("error: $it")
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
                dateOfBirth = dateOfBirth,
                age = age,
                modifier = Modifier.padding(innerPadding),
                onDateOfBirthChange = ::onDateOfBirthChange,
                validateIntegerUseCase = validateIntegerUseCase,
            )
        },
    )
}