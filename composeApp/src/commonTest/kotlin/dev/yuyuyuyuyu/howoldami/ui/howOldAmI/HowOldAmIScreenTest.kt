package dev.yuyuyuyuyu.howoldami.ui.howOldAmI

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.v2.runComposeUiTest
import dev.yuyuyuyuyu.howoldami.domain.useCases.CalculateAgeUseCase
import kotlin.test.Test
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn

@OptIn(ExperimentalTestApi::class, ExperimentalTime::class)
class HowOldAmIScreenTest {
    @Test
    fun `shows no age before a date of birth is entered`() = runComposeUiTest {
        showScreen(today = LocalDate(2026, 6, 21))

        onNodeWithTag("yearInput").assertIsDisplayed()
        onNodeWithTag("monthInput").assertIsDisplayed()
        onNodeWithTag("dayInput").assertIsDisplayed()
        onNodeWithTag("ageText").assertContentDescriptionEquals("")
    }

    @Test
    fun `shows the full age when the birthday already passed this year`() = runComposeUiTest {
        showScreen(today = LocalDate(2026, 6, 21))

        enterDateOfBirth(year = "1990", month = "1", day = "1")

        onNodeWithTag("ageText").assertContentDescriptionEquals("36")
    }

    @Test
    fun `does not subtract one on the birthday itself`() = runComposeUiTest {
        showScreen(today = LocalDate(2026, 6, 21))

        enterDateOfBirth(year = "2000", month = "6", day = "21")

        onNodeWithTag("ageText").assertContentDescriptionEquals("26")
    }

    @Test
    fun `subtracts one when the birthday has not occurred yet this year`() = runComposeUiTest {
        showScreen(today = LocalDate(2026, 6, 21))

        enterDateOfBirth(year = "2000", month = "12", day = "31")

        onNodeWithTag("ageText").assertContentDescriptionEquals("25")
    }

    @Test
    fun `shows no age while the date of birth is still incomplete`() = runComposeUiTest {
        showScreen(today = LocalDate(2026, 6, 21))

        onNodeWithTag("yearInput").performTextInput("1990")

        onNodeWithTag("ageText").assertContentDescriptionEquals("")
    }

    @Test
    fun `shows no age for a date that does not exist in the calendar`() = runComposeUiTest {
        showScreen(today = LocalDate(2026, 6, 21))

        enterDateOfBirth(year = "1990", month = "2", day = "30")

        onNodeWithTag("ageText").assertContentDescriptionEquals("")
    }

    @Test
    fun `shows no age when the month is out of range`() = runComposeUiTest {
        showScreen(today = LocalDate(2026, 6, 21))

        enterDateOfBirth(year = "1990", month = "0", day = "1")

        onNodeWithTag("ageText").assertContentDescriptionEquals("")
    }

    @Test
    fun `shows no age when the day is out of range`() = runComposeUiTest {
        showScreen(today = LocalDate(2026, 6, 21))

        enterDateOfBirth(year = "1990", month = "1", day = "0")

        onNodeWithTag("ageText").assertContentDescriptionEquals("")
    }

    @Test
    fun `recalculates the age when the date of birth is edited`() = runComposeUiTest {
        showScreen(today = LocalDate(2026, 6, 21))

        enterDateOfBirth(year = "2000", month = "1", day = "1")
        onNodeWithTag("ageText").assertContentDescriptionEquals("26")

        // Typing appends, so the month becomes 12 and the birthday moves past today
        onNodeWithTag("monthInput").performTextInput("2")

        onNodeWithTag("ageText").assertContentDescriptionEquals("25")
    }

    /**
     * Shows the real screen, wired to the real use case and view model. Only the clock is
     * substituted, pinning "today" to [today] so the calculated age does not depend on the
     * wall clock of the machine running the test. The time zone is pinned along with it,
     * because a fixed instant still lands on a different date either side of midnight.
     */
    private fun ComposeUiTest.showScreen(today: LocalDate) {
        val fixedClock =
            object : Clock {
                override fun now() = today.atStartOfDayIn(TimeZone.UTC)
            }
        val viewModel =
            HowOldAmIViewModelImpl(
                CalculateAgeUseCase(clock = fixedClock, timeZone = TimeZone.UTC)
            )

        setContent {
            HowOldAmIScreen(viewModel)
        }
    }

    private fun ComposeUiTest.enterDateOfBirth(year: String, month: String, day: String) {
        onNodeWithTag("yearInput").performTextInput(year)
        onNodeWithTag("monthInput").performTextInput(month)
        onNodeWithTag("dayInput").performTextInput(day)
    }
}
