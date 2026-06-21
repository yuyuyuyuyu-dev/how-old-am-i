package dev.yuyuyuyuyu.howoldami.ui.howOldAmI

import dev.yuyuyuyuyu.howoldami.domain.useCases.CalculateAgeUseCase
import dev.yuyuyuyuyu.howoldami.ui.model.DateOfBirth
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn

@OptIn(ExperimentalTime::class)
class HowOldAmIViewModelImplTest {
    @Test
    fun `onDateOfBirthChanged stores the input and the calculated age for a valid date`() {
        val viewModel = viewModelWithToday(LocalDate(2026, 6, 21))
        val validDate = DateOfBirth(year = "1990", month = "1", day = "1")

        viewModel.onDateOfBirthChanged(validDate)

        val state = viewModel.uiState.value
        assertEquals(validDate, state.dateOfBirth)
        assertEquals("36", state.age)
    }

    @Test
    fun `onDateOfBirthChanged clears the age for a non-existent calendar date`() {
        val viewModel = viewModelWithToday(LocalDate(2026, 6, 21))
        val invalidDate = DateOfBirth(year = "1990", month = "2", day = "30")

        viewModel.onDateOfBirthChanged(invalidDate)

        val state = viewModel.uiState.value
        assertEquals(invalidDate, state.dateOfBirth)
        assertEquals("", state.age)
    }

    @Test
    fun `onDateOfBirthChanged clears the age when the year is not a number`() {
        val viewModel = viewModelWithToday(LocalDate(2026, 6, 21))
        val nonNumericYear = DateOfBirth(year = "nineteen", month = "1", day = "1")

        viewModel.onDateOfBirthChanged(nonNumericYear)

        val state = viewModel.uiState.value
        assertEquals(nonNumericYear, state.dateOfBirth)
        assertEquals("", state.age)
    }

    @Test
    fun `onDateOfBirthChanged clears the age when the month is out of range`() {
        val viewModel = viewModelWithToday(LocalDate(2026, 6, 21))
        val zeroMonth = DateOfBirth(year = "1990", month = "0", day = "1")

        viewModel.onDateOfBirthChanged(zeroMonth)

        assertEquals("", viewModel.uiState.value.age)
    }

    @Test
    fun `onDateOfBirthChanged clears the age when the day is out of range`() {
        val viewModel = viewModelWithToday(LocalDate(2026, 6, 21))
        val zeroDay = DateOfBirth(year = "1990", month = "1", day = "0")

        viewModel.onDateOfBirthChanged(zeroDay)

        assertEquals("", viewModel.uiState.value.age)
    }

    @Test
    fun `onDateOfBirthChanged clears a stale age when the input becomes incomplete`() {
        val viewModel = viewModelWithToday(LocalDate(2026, 6, 21))

        viewModel.onDateOfBirthChanged(DateOfBirth(year = "1990", month = "1", day = "1"))
        assertEquals("36", viewModel.uiState.value.age)

        viewModel.onDateOfBirthChanged(DateOfBirth(year = "", month = "1", day = "1"))
        assertEquals("", viewModel.uiState.value.age)
    }

    private fun viewModelWithToday(today: LocalDate): HowOldAmIViewModelImpl {
        val fixedClock =
            object : Clock {
                override fun now() = today.atStartOfDayIn(TimeZone.UTC)
            }
        val useCase = CalculateAgeUseCase(clock = fixedClock, timeZone = TimeZone.UTC)
        return HowOldAmIViewModelImpl(useCase)
    }
}
