package dev.yuyuyuyuyu.howoldami.ui.howOldAmI

import dev.yuyuyuyuyu.howoldami.domain.useCases.CalculateAgeUseCase
import dev.yuyuyuyuyu.howoldami.ui.model.DateOfBirth
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals

class HowOldAmIViewModelImplTest {
    @Test
    fun `onDateOfBirthChanged updates uiState with correct age when valid date is provided`() {
        val useCase = CalculateAgeUseCase()
        val viewModel = HowOldAmIViewModelImpl(useCase)

        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val birthYear = today.year - 20

        val validDate =
            DateOfBirth(
                year = birthYear.toString(),
                month = "1",
                day = "1",
            )

        viewModel.onDateOfBirthChanged(validDate)

        val state = viewModel.uiState.value
        assertEquals(validDate, state.dateOfBirth)
        assertEquals("20", state.age)
    }

    @Test
    fun `onDateOfBirthChanged clears age when invalid date is provided`() {
        val useCase = CalculateAgeUseCase()
        val viewModel = HowOldAmIViewModelImpl(useCase)

        val invalidDate =
            DateOfBirth(
                year = "1990",
                month = "13",
                day = "1",
            )

        viewModel.onDateOfBirthChanged(invalidDate)

        val state = viewModel.uiState.value
        assertEquals(invalidDate, state.dateOfBirth)
        assertEquals("", state.age)
    }
}
