package dev.yuyuyuyuyu.howoldami.domain.useCases

import androidx.annotation.IntRange
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.tatarka.inject.annotations.Inject

@OptIn(ExperimentalTime::class)
@Inject
class CalculateAgeUseCase(
    private val clock: Clock = Clock.System,
    private val timeZone: TimeZone = TimeZone.currentSystemDefault()
) {
    operator fun invoke(
        year: Int,
        @IntRange(from = 1, to = 12) month: Int,
        @IntRange(from = 1, to = 31) day: Int
    ): Int {
        val today = clock.now().toLocalDateTime(timeZone)

        var age = today.year - year

        val birthdayThisYear =
            LocalDate(
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
