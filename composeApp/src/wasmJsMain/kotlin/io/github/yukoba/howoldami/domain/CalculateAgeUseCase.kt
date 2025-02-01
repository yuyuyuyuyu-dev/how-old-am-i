package io.github.yukoba.howoldami.domain

import io.github.yukoba.howoldami.data.types.DateOfBirth
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun calculateAgeUseCase(
    dateOfBirth: DateOfBirth,
    onSuccess: (String) -> Unit,
    onFailure: (Exception) -> Unit = {},
) {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    try {
        var age = today.year - dateOfBirth.year.toInt()

        val birthdayThisYear = LocalDate(
            year = today.year,
            monthNumber = dateOfBirth.month.toInt(),
            dayOfMonth = dateOfBirth.day.toInt(),
        )
        if (today.date < birthdayThisYear) {
            age -= 1
        }

        onSuccess(age.toString())
    } catch (e: Exception) {
        onFailure(e)
    }
}
