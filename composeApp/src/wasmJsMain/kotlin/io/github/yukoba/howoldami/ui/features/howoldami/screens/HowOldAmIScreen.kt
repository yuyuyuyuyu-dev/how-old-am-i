package io.github.yukoba.howoldami.ui.features.howoldami.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.yukoba.howoldami.ui.components.types.DateOfBirth
import io.github.yukoba.howoldami.usecase.ValidateIntegerUseCase
import io.github.yukoba.howoldami.usecase.calculateAgeUseCase

@Composable
fun HowOldAmIScreen(
) {
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

    MainScreen(
        dateOfBirth = dateOfBirth,
        age = age,
        onDateOfBirthChange = ::onDateOfBirthChange,
        validateIntegerUseCase = validateIntegerUseCase,
    )
}