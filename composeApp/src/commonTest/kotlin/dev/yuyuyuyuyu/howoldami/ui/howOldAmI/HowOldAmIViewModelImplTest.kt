package dev.yuyuyuyuyu.howoldami.ui.howOldAmI

import dev.yuyuyuyuyu.howoldami.domain.useCases.CalculateAgeUseCase
import dev.yuyuyuyuyu.howoldami.ui.model.DateOfBirth
import kotlin.test.Test
import kotlin.test.assertEquals

class HowOldAmIViewModelImplTest {
    @Test
    fun `onDateOfBirthChanged updates uiState with correct age when valid date is provided`() {
        val useCase = CalculateAgeUseCase()
        val viewModel = HowOldAmIViewModelImpl(useCase)

        val validDate =
            DateOfBirth(
                year = "1990",
                month = "1",
                day = "1",
            )

        val expectedAge = useCase(1990, 1, 1).toString()

        viewModel.onDateOfBirthChanged(validDate)

        val state = viewModel.uiState.value
        assertEquals(validDate, state.dateOfBirth)
        assertEquals(expectedAge, state.age)
    }

    @Test
    fun `onDateOfBirthChanged clears age when invalid date is provided`() {
        val useCase = CalculateAgeUseCase()
        val viewModel = HowOldAmIViewModelImpl(useCase)

        val invalidDate =
            DateOfBirth(
                year = "1990",
                month = "2",
                day = "30",
            )

        viewModel.onDateOfBirthChanged(invalidDate)

        val state = viewModel.uiState.value
        assertEquals(invalidDate, state.dateOfBirth)
        assertEquals("", state.age)
    }
}
