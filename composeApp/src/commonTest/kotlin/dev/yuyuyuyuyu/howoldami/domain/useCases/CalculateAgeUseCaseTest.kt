package dev.yuyuyuyuyu.howoldami.domain.useCases

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn

@OptIn(ExperimentalTime::class)
class CalculateAgeUseCaseTest {
    @Test
    fun `returns full age when the birthday already passed this year`() {
        val useCase = useCaseWithToday(LocalDate(2026, 6, 21))

        assertEquals(36, useCase(year = 1990, month = 1, day = 1))
    }

    @Test
    fun `does not subtract one on the birthday itself`() {
        val useCase = useCaseWithToday(LocalDate(2026, 6, 21))

        assertEquals(26, useCase(year = 2000, month = 6, day = 21))
    }

    @Test
    fun `subtracts one when the birthday has not occurred yet this year`() {
        val useCase = useCaseWithToday(LocalDate(2026, 6, 21))

        assertEquals(25, useCase(year = 2000, month = 12, day = 31))
    }

    @Test
    fun `subtracts one on the day before the birthday`() {
        val useCase = useCaseWithToday(LocalDate(2026, 6, 20))

        assertEquals(25, useCase(year = 2000, month = 6, day = 21))
    }

    @Test
    fun `returns zero for someone born earlier in the current year`() {
        val useCase = useCaseWithToday(LocalDate(2026, 6, 21))

        assertEquals(0, useCase(year = 2026, month = 1, day = 1))
    }

    /**
     * Builds a [CalculateAgeUseCase] whose notion of "today" is pinned to [today], so age
     * calculation is deterministic regardless of the wall clock and time zone of the machine
     * (CI, browser, or local) running the test.
     */
    private fun useCaseWithToday(today: LocalDate): CalculateAgeUseCase {
        val fixedClock =
            object : Clock {
                override fun now() = today.atStartOfDayIn(TimeZone.UTC)
            }
        return CalculateAgeUseCase(clock = fixedClock, timeZone = TimeZone.UTC)
    }
}
