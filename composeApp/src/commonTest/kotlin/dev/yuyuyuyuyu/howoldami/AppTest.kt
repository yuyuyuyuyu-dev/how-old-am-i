package dev.yuyuyuyuyu.howoldami

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.assertIsNotFocused
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.click
import androidx.compose.ui.test.isFocused
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.v2.runComposeUiTest
import dev.yuyuyuyuyu.howoldami.domain.useCases.CalculateAgeUseCase
import dev.yuyuyuyuyu.howoldami.ui.howOldAmI.HowOldAmIScreen
import dev.yuyuyuyuyu.howoldami.ui.howOldAmI.HowOldAmIViewModelImpl
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.asTimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTestApi::class, ExperimentalTime::class)
class AppTest {
    @Test
    fun `shows no age before a date of birth is entered`() =
        runComposeUiTest {
            setContent { App() }

            onNodeWithTag("yearInput").assertIsDisplayed()
            onNodeWithTag("monthInput").assertIsDisplayed()
            onNodeWithTag("dayInput").assertIsDisplayed()
            assertAgeInApp("")
        }

    @Test
    fun `calculates the age from the current date of the running app`() =
        runComposeUiTest {
            val thisYear =
                Clock.System
                    .now()
                    .toLocalDateTime(TimeZone.currentSystemDefault())
                    .year
            setContent { App() }

            enterDateOfBirth(year = "${thisYear - 30}", month = "1", day = "1")

            assertAgeInApp("30")
        }

    @Test
    fun `shows no age while the date of birth is still incomplete`() =
        runComposeUiTest {
            setContent { App() }

            onNodeWithTag("yearInput").performTextInput("1990")

            assertAgeInApp("")
        }

    @Test
    fun `shows no age for a date that does not exist in the calendar`() =
        runComposeUiTest {
            setContent { App() }

            enterDateOfBirth(year = "1990", month = "2", day = "30")

            assertAgeInApp("")
        }

    @Test
    fun `shows no age when the month is out of range`() =
        runComposeUiTest {
            setContent { App() }

            enterDateOfBirth(year = "1990", month = "0", day = "1")

            assertAgeInApp("")
        }

    @Test
    fun `shows no age when the day is out of range`() =
        runComposeUiTest {
            setContent { App() }

            enterDateOfBirth(year = "1990", month = "1", day = "0")

            assertAgeInApp("")
        }

    @Test
    fun `ignores letters typed into the year`() =
        runComposeUiTest {
            setContent { App() }

            onNodeWithTag("yearInput").performTextInput("abc")

            onNodeWithTag("yearInput").assertTextEquals("")
        }

    @Test
    fun `ignores letters typed into the month`() =
        runComposeUiTest {
            setContent { App() }

            onNodeWithTag("monthInput").performTextInput("abc")

            onNodeWithTag("monthInput").assertTextEquals("")
        }

    @Test
    fun `ignores letters typed into the day`() =
        runComposeUiTest {
            setContent { App() }

            onNodeWithTag("dayInput").performTextInput("abc")

            onNodeWithTag("dayInput").assertTextEquals("")
        }

    @Test
    fun `keeps the month when the next digit would make it greater than 12`() =
        runComposeUiTest {
            setContent { App() }

            onNodeWithTag("monthInput").performTextInput("1")
            onNodeWithTag("monthInput").performTextInput("3")

            onNodeWithTag("monthInput").assertTextEquals("1")
        }

    @Test
    fun `keeps the day when the next digit would make it greater than 31`() =
        runComposeUiTest {
            setContent { App() }

            onNodeWithTag("dayInput").performTextInput("3")
            onNodeWithTag("dayInput").performTextInput("2")

            onNodeWithTag("dayInput").assertTextEquals("3")
        }

    @Test
    fun `moves the focus to the month after four digits of the year`() =
        runComposeUiTest {
            setContent { App() }

            onNodeWithTag("yearInput").performTextInput("1990")

            onNodeWithTag("monthInput").assertIsFocused()
        }

    @Test
    fun `moves the focus to the day after two digits of the month`() =
        runComposeUiTest {
            setContent { App() }

            onNodeWithTag("monthInput").performTextInput("12")

            onNodeWithTag("dayInput").assertIsFocused()
        }

    @Test
    fun `clears the focus after two digits of the day`() =
        runComposeUiTest {
            setContent { App() }

            onNodeWithTag("dayInput").performTextInput("31")

            onNodeWithTag("dayInput").assertIsNotFocused()
        }

    @Test
    fun `clears the focus when the outside of the inputs is clicked`() =
        runComposeUiTest {
            setContent { App() }
            onNodeWithTag("yearInput").performClick()
            onNodeWithTag("yearInput").assertIsFocused()

            onRoot().performTouchInput { click(bottomCenter - Offset(0f, 1f)) }

            onAllNodes(isFocused()).assertCountEquals(0)
        }

    @Test
    fun `opens the source code repository from the menu`() =
        runComposeUiTest {
            val openedUris = mutableListOf<String>()
            val uriHandler =
                object : UriHandler {
                    override fun openUri(uri: String) {
                        openedUris += uri
                    }
                }
            setContent {
                CompositionLocalProvider(LocalUriHandler provides uriHandler) {
                    App()
                }
            }

            onNodeWithTag("SimpleTopAppBar.MenuButton").performClick()
            onNodeWithTag("SimpleTopAppBar.SourceCodeButton").performClick()

            assertEquals(listOf("https://github.com/yuyuyuyuyu-dev/how-old-am-i"), openedUris)
        }

    @Test
    @Ignore
    fun `shows no age after the only digit of the day is deleted`() =
        runComposeUiTest {
            showScreen(today = LocalDate(2026, 6, 21))
            enterDateOfBirth(year = "2000", month = "1", day = "1")

            onNodeWithTag("dayInput").performTextClearance()

            onNodeWithTag("dayInput").assertTextEquals("")
            onNodeWithTag("ageText").assertContentDescriptionEquals("")
        }

    @Test
    @Ignore
    fun `empties the year after all of its digits are deleted`() =
        runComposeUiTest {
            showScreen(today = LocalDate(2026, 6, 21))
            onNodeWithTag("yearInput").performTextInput("2000")

            onNodeWithTag("yearInput").performTextClearance()

            onNodeWithTag("yearInput").assertTextEquals("")
        }

    @Test
    fun `shows the full age when the birthday already passed this year`() =
        runComposeUiTest {
            showScreen(today = LocalDate(2026, 6, 21))

            enterDateOfBirth(year = "1990", month = "1", day = "1")

            onNodeWithTag("ageText").assertContentDescriptionEquals("36")
        }

    @Test
    fun `does not subtract one on the birthday itself`() =
        runComposeUiTest {
            showScreen(today = LocalDate(2026, 6, 21))

            enterDateOfBirth(year = "2000", month = "6", day = "21")

            onNodeWithTag("ageText").assertContentDescriptionEquals("26")
        }

    @Test
    fun `subtracts one when the birthday has not occurred yet this year`() =
        runComposeUiTest {
            showScreen(today = LocalDate(2026, 6, 21))

            enterDateOfBirth(year = "2000", month = "12", day = "31")

            onNodeWithTag("ageText").assertContentDescriptionEquals("25")
        }

    @Test
    fun `subtracts one on the day before the birthday`() =
        runComposeUiTest {
            showScreen(today = LocalDate(2026, 6, 21))

            enterDateOfBirth(year = "2000", month = "6", day = "22")

            onNodeWithTag("ageText").assertContentDescriptionEquals("25")
        }

    @Test
    fun `does not subtract one on the day after the birthday`() =
        runComposeUiTest {
            showScreen(today = LocalDate(2026, 6, 21))

            enterDateOfBirth(year = "2000", month = "6", day = "20")

            onNodeWithTag("ageText").assertContentDescriptionEquals("26")
        }

    @Test
    fun `does not subtract one for an earlier month with a later day`() =
        runComposeUiTest {
            showScreen(today = LocalDate(2026, 6, 21))

            enterDateOfBirth(year = "2000", month = "5", day = "31")

            onNodeWithTag("ageText").assertContentDescriptionEquals("26")
        }

    @Test
    fun `subtracts one for a later month with an earlier day`() =
        runComposeUiTest {
            showScreen(today = LocalDate(2026, 6, 21))

            enterDateOfBirth(year = "2000", month = "7", day = "1")

            onNodeWithTag("ageText").assertContentDescriptionEquals("25")
        }

    @Test
    fun `accepts a month and a day with a leading zero`() =
        runComposeUiTest {
            showScreen(today = LocalDate(2026, 6, 21))

            enterDateOfBirth(year = "1990", month = "01", day = "01")

            onNodeWithTag("ageText").assertContentDescriptionEquals("36")
        }

    @Test
    fun `recalculates the age when the date of birth is edited`() =
        runComposeUiTest {
            showScreen(today = LocalDate(2026, 6, 21))

            enterDateOfBirth(year = "2000", month = "1", day = "1")
            onNodeWithTag("ageText").assertContentDescriptionEquals("26")

            onNodeWithTag("monthInput").performTextInput("2")

            onNodeWithTag("ageText").assertContentDescriptionEquals("25")
        }

    @Test
    fun `decides today by the date in the time zone of the user`() =
        runComposeUiTest {
            showScreen(
                now = LocalDate(2026, 6, 20).atTime(15, 0).toInstant(TimeZone.UTC),
                timeZone = UtcOffset(hours = 9).asTimeZone(),
            )

            enterDateOfBirth(year = "2000", month = "6", day = "21")

            onNodeWithTag("ageText").assertContentDescriptionEquals("26")
        }

    @Test
    fun `shows the age of someone born on a leap day when today is a leap day`() =
        runComposeUiTest {
            showScreen(today = LocalDate(2024, 2, 29))

            enterDateOfBirth(year = "2000", month = "2", day = "29")

            onNodeWithTag("ageText").assertContentDescriptionEquals("24")
        }

    @Test
    @Ignore
    fun `shows the age of someone born on a leap day when this year has no leap day`() =
        runComposeUiTest {
            showScreen(today = LocalDate(2026, 6, 21))

            enterDateOfBirth(year = "2000", month = "2", day = "29")

            onNodeWithTag("ageText").assertContentDescriptionEquals("26")
        }

    @Test
    @Ignore
    fun `shows no age for a leap day in a birth year without one when this year has one`() =
        runComposeUiTest {
            showScreen(today = LocalDate(2024, 6, 21))

            enterDateOfBirth(year = "2023", month = "2", day = "29")

            onNodeWithTag("ageText").assertContentDescriptionEquals("")
        }

    private fun ComposeUiTest.assertAgeInApp(age: String) {
        onNodeWithTag("ageText", useUnmergedTree = true).assertContentDescriptionEquals(age)
    }

    private fun ComposeUiTest.showScreen(today: LocalDate) {
        showScreen(now = today.atStartOfDayIn(TimeZone.UTC), timeZone = TimeZone.UTC)
    }

    private fun ComposeUiTest.showScreen(
        now: Instant,
        timeZone: TimeZone,
    ) {
        val fixedClock =
            object : Clock {
                override fun now() = now
            }
        val viewModel = HowOldAmIViewModelImpl(CalculateAgeUseCase(fixedClock, timeZone))

        setContent {
            HowOldAmIScreen(viewModel)
        }
    }

    private fun ComposeUiTest.enterDateOfBirth(
        year: String,
        month: String,
        day: String,
    ) {
        onNodeWithTag("yearInput").performTextInput(year)
        onNodeWithTag("monthInput").performTextInput(month)
        onNodeWithTag("dayInput").performTextInput(day)
    }
}
