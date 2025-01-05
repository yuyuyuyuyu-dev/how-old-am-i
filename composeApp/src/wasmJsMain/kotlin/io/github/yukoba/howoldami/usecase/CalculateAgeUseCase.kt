package io.github.yukoba.howoldami.usecase

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun calculateAgeUseCase(
    year: String,
    month: String,
    day: String,
    onSucceeded: (String) -> Unit,
    onFailed: (Exception) -> Unit = {},
) {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    try {
        var age = today.year - year.toInt()

        val birthdayThisYear = LocalDate(today.year, month.toInt(), day.toInt())
        if (today.date < birthdayThisYear) {
            age -= 1
        }

        onSucceeded(age.toString())
    } catch (e: Exception) {
        onFailed(e)
    }
}