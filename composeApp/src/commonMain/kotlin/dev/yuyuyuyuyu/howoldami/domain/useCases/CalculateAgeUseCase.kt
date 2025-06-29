package dev.yuyuyuyuyu.howoldami.domain.useCases

import androidx.annotation.IntRange
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class CalculateAgeUseCase() {
    @OptIn(ExperimentalTime::class)
    operator fun invoke(
        year: Int,
        @IntRange(from = 1, to = 12) month: Int,
        @IntRange(from = 1, to = 31) day: Int,
    ): Int {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        var age = today.year - year

        val birthdayThisYear = LocalDate(
            year = today.year,
            month = month,
            day = day
        )
        if (today.date < birthdayThisYear) {
            age -= 1
        }

        return age
    }
}
