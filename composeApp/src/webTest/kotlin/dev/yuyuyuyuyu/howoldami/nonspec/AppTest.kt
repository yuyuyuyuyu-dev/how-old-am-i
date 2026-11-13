package dev.yuyuyuyuyu.howoldami.nonspec

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.assertIsNotFocused
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.click
import androidx.compose.ui.test.isFocused
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.v2.runComposeUiTest
import dev.yuyuyuyuyu.howoldami.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.asTimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.time.TimeSource

@OptIn(ExperimentalTestApi::class, ExperimentalTime::class)
class AppTest {
    @Test
    fun `should show the title, the prompt, the inputs, and no age when the app starts`() =
        runComposeUiTest {
            // Act
            setContent { App() }

            // Assert
            awaitNodeWithText("How old am I").assertIsDisplayed()
            awaitNodeWithText("Please input your date of birth.").assertIsDisplayed()
            onNodeWithTag("yearInput").assertIsDisplayed()
            onNodeWithTag("monthInput").assertIsDisplayed()
            onNodeWithTag("dayInput").assertIsDisplayed()
            assertNoAge()
        }

    @Test
    fun `should keep the date of birth after coming back from the open source licenses`() =
        runComposeUiTest {
            // Arrange
            showApp(today = LocalDate(2026, 6, 21))
            enterDateOfBirth(year = "2000", month = "6", day = "21")

            // Act
            onNodeWithTag("SimpleTopAppBar.MenuButton").performClick()
            onNodeWithTag("SimpleTopAppBar.OpenSourceLicensesButton").performClick()
            onNodeWithTag("SimpleTopAppBar.NavigateBackButton").performClick()

            // Assert
            onNodeWithTag("yearInput").assertTextEquals("2000")
            onNodeWithTag("monthInput").assertTextEquals("6")
            onNodeWithTag("dayInput").assertTextEquals("21")
            assertAge(26)
        }

    @Test
    fun `should calculate the age from the current date of the running app`() =
        runComposeUiTest {
            // Arrange
            val thisYear =
                Clock.System
                    .now()
                    .toLocalDateTime(TimeZone.currentSystemDefault())
                    .year
            setContent { App() }

            // Act
            enterDateOfBirth(year = "${thisYear - 30}", month = "1", day = "1")

            // Assert
            assertAge(30)
        }

    @Test
    fun `should show no age while the date of birth is incomplete`() =
        runComposeUiTest {
            // Arrange
            setContent { App() }

            // Act
            onNodeWithTag("yearInput").performTextInput("1990")

            // Assert
            assertNoAge()
        }

    @Test
    fun `should show no age for a date that does not exist in the calendar`() =
        runComposeUiTest {
            // Arrange
            setContent { App() }

            // Act
            enterDateOfBirth(year = "1990", month = "2", day = "30")

            // Assert
            assertNoAge()
        }

    @Test
    fun `should show no age for a year the calendar cannot represent`() =
        runComposeUiTest {
            // Arrange
            setContent { App() }

            // Act
            enterDateOfBirth(year = "1000000000", month = "1", day = "1")

            // Assert
            assertNoAge()
        }

    @Test
    fun `should show no age when the month is zero`() =
        runComposeUiTest {
            // Arrange
            setContent { App() }

            // Act
            enterDateOfBirth(year = "1990", month = "0", day = "1")

            // Assert
            assertNoAge()
        }

    @Test
    fun `should show no age when the day is zero`() =
        runComposeUiTest {
            // Arrange
            setContent { App() }

            // Act
            enterDateOfBirth(year = "1990", month = "1", day = "0")

            // Assert
            assertNoAge()
        }

    @Test
    fun `should ignore letters typed into the year`() =
        runComposeUiTest {
            // Arrange
            setContent { App() }

            // Act
            onNodeWithTag("yearInput").performTextInput("abc")

            // Assert
            onNodeWithTag("yearInput").assertTextEquals("")
        }

    @Test
    fun `should ignore letters typed into the month`() =
        runComposeUiTest {
            // Arrange
            setContent { App() }

            // Act
            onNodeWithTag("monthInput").performTextInput("abc")

            // Assert
            onNodeWithTag("monthInput").assertTextEquals("")
        }

    @Test
    fun `should ignore letters typed into the day`() =
        runComposeUiTest {
            // Arrange
            setContent { App() }

            // Act
            onNodeWithTag("dayInput").performTextInput("abc")

            // Assert
            onNodeWithTag("dayInput").assertTextEquals("")
        }

    @Test
    fun `should keep the month when the next digit would make it greater than 12`() =
        runComposeUiTest {
            // Arrange
            setContent { App() }
            onNodeWithTag("monthInput").performTextInput("1")

            // Act
            onNodeWithTag("monthInput").performTextInput("3")

            // Assert
            onNodeWithTag("monthInput").assertTextEquals("1")
        }

    @Test
    fun `should keep the day when the next digit would make it greater than 31`() =
        runComposeUiTest {
            // Arrange
            setContent { App() }
            onNodeWithTag("dayInput").performTextInput("3")

            // Act
            onNodeWithTag("dayInput").performTextInput("2")

            // Assert
            onNodeWithTag("dayInput").assertTextEquals("3")
        }

    @Test
    fun `should empty the year and show no age when the year is deleted`() =
        runComposeUiTest {
            // Arrange
            setContent { App() }
            enterDateOfBirth(year = "2000", month = "1", day = "1")

            // Act
            onNodeWithTag("yearInput").performTextClearance()

            // Assert
            onNodeWithTag("yearInput").assertTextEquals("")
            assertNoAge()
        }

    @Test
    fun `should empty the month and show no age when the month is deleted`() =
        runComposeUiTest {
            // Arrange
            setContent { App() }
            enterDateOfBirth(year = "2000", month = "1", day = "1")

            // Act
            onNodeWithTag("monthInput").performTextClearance()

            // Assert
            onNodeWithTag("monthInput").assertTextEquals("")
            assertNoAge()
        }

    @Test
    fun `should empty the day and show no age when the day is deleted`() =
        runComposeUiTest {
            // Arrange
            setContent { App() }
            enterDateOfBirth(year = "2000", month = "1", day = "1")

            // Act
            onNodeWithTag("dayInput").performTextClearance()

            // Assert
            onNodeWithTag("dayInput").assertTextEquals("")
            assertNoAge()
        }

    @Test
    fun `should move the focus to the month after four digits of the year`() =
        runComposeUiTest {
            // Arrange
            setContent { App() }

            // Act
            onNodeWithTag("yearInput").performTextInput("1990")

            // Assert
            onNodeWithTag("monthInput").assertIsFocused()
        }

    @Test
    fun `should move the focus to the day after two digits of the month`() =
        runComposeUiTest {
            // Arrange
            setContent { App() }

            // Act
            onNodeWithTag("monthInput").performTextInput("12")

            // Assert
            onNodeWithTag("dayInput").assertIsFocused()
        }

    @Test
    fun `should clear the focus after two digits of the day`() =
        runComposeUiTest {
            // Arrange
            setContent { App() }

            // Act
            onNodeWithTag("dayInput").performTextInput("31")

            // Assert
            onNodeWithTag("dayInput").assertIsNotFocused()
        }

    @Test
    fun `should clear the focus when the outside of the inputs is clicked`() =
        runComposeUiTest {
            // Arrange
            setContent { App() }
            onNodeWithTag("yearInput").performClick()

            // Act
            onRoot().performTouchInput { click(bottomCenter - Offset(0f, 1f)) }

            // Assert
            onAllNodes(isFocused()).assertCountEquals(0)
        }

    @Test
    fun `should open the source code repository from the menu`() =
        runComposeUiTest {
            // Arrange
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

            // Act
            onNodeWithTag("SimpleTopAppBar.MenuButton").performClick()
            onNodeWithTag("SimpleTopAppBar.SourceCodeButton").performClick()

            // Assert
            assertEquals(listOf("https://github.com/yuyuyuyuyu-dev/how-old-am-i"), openedUris)
        }

    @Test
    fun `should not subtract one when the birthday already passed this year`() =
        runComposeUiTest {
            // Arrange
            showApp(today = LocalDate(2026, 6, 21))

            // Act
            enterDateOfBirth(year = "1990", month = "1", day = "1")

            // Assert
            assertAge(36)
        }

    @Test
    fun `should not subtract one on the birthday itself`() =
        runComposeUiTest {
            // Arrange
            showApp(today = LocalDate(2026, 6, 21))

            // Act
            enterDateOfBirth(year = "2000", month = "6", day = "21")

            // Assert
            assertAge(26)
        }

    @Test
    fun `should subtract one when the birthday has not come yet this year`() =
        runComposeUiTest {
            // Arrange
            showApp(today = LocalDate(2026, 6, 21))

            // Act
            enterDateOfBirth(year = "2000", month = "12", day = "31")

            // Assert
            assertAge(25)
        }

    @Test
    fun `should subtract one on the day before the birthday`() =
        runComposeUiTest {
            // Arrange
            showApp(today = LocalDate(2026, 6, 21))

            // Act
            enterDateOfBirth(year = "2000", month = "6", day = "22")

            // Assert
            assertAge(25)
        }

    @Test
    fun `should not subtract one on the day after the birthday`() =
        runComposeUiTest {
            // Arrange
            showApp(today = LocalDate(2026, 6, 21))

            // Act
            enterDateOfBirth(year = "2000", month = "6", day = "20")

            // Assert
            assertAge(26)
        }

    @Test
    fun `should not subtract one for an earlier month with a later day`() =
        runComposeUiTest {
            // Arrange
            showApp(today = LocalDate(2026, 6, 21))

            // Act
            enterDateOfBirth(year = "2000", month = "5", day = "31")

            // Assert
            assertAge(26)
        }

    @Test
    fun `should subtract one for a later month with an earlier day`() =
        runComposeUiTest {
            // Arrange
            showApp(today = LocalDate(2026, 6, 21))

            // Act
            enterDateOfBirth(year = "2000", month = "7", day = "1")

            // Assert
            assertAge(25)
        }

    @Test
    fun `should show zero on the day of birth`() =
        runComposeUiTest {
            // Arrange
            showApp(today = LocalDate(2026, 6, 21))

            // Act
            enterDateOfBirth(year = "2026", month = "6", day = "21")

            // Assert
            assertAge(0)
        }

    @Test
    fun `should show a negative age for a future date of birth whose birthday already passed this year`() =
        runComposeUiTest {
            // Arrange
            showApp(today = LocalDate(2026, 6, 21))

            // Act
            enterDateOfBirth(year = "2030", month = "1", day = "1")

            // Assert
            assertAge(-4)
        }

    @Test
    fun `should subtract one from a negative age when the birthday has not come yet this year`() =
        runComposeUiTest {
            // Arrange
            showApp(today = LocalDate(2026, 6, 21))

            // Act
            enterDateOfBirth(year = "2030", month = "12", day = "31")

            // Assert
            assertAge(-5)
        }

    @Test
    fun `should accept a month and a day with a leading zero`() =
        runComposeUiTest {
            // Arrange
            showApp(today = LocalDate(2026, 6, 21))

            // Act
            enterDateOfBirth(year = "1990", month = "01", day = "01")

            // Assert
            assertAge(36)
        }

    @Test
    fun `should recalculate the age when the date of birth is edited`() =
        runComposeUiTest {
            // Arrange
            showApp(today = LocalDate(2026, 6, 21))
            enterDateOfBirth(year = "2000", month = "1", day = "1")

            // Act
            onNodeWithTag("monthInput").performTextInput("2")

            // Assert
            assertAge(25)
        }

    @Test
    fun `should decide today by the date in the time zone of the user`() =
        runComposeUiTest {
            // Arrange
            showApp(
                now = LocalDate(2026, 6, 20).atTime(15, 0).toInstant(TimeZone.UTC),
                timeZone = UtcOffset(hours = 9).asTimeZone(),
            )

            // Act
            enterDateOfBirth(year = "2000", month = "6", day = "21")

            // Assert
            assertAge(26)
        }

    @Test
    fun `should use the date of a browser ahead of UTC`() =
        runComposeUiTest {
            inBrowserAtNoonUtc(minutesBehindUtc = -840.0) {
                // Arrange
                setContent { App() }

                // Act
                enterDateOfBirth(year = "2000", month = "6", day = "21")

                // Assert
                assertAge(26)
            }
        }

    @Test
    fun `should use the date of a browser behind UTC`() =
        runComposeUiTest {
            inBrowserAtNoonUtc(minutesBehindUtc = 720.0) {
                // Arrange
                setContent { App() }

                // Act
                enterDateOfBirth(year = "2000", month = "6", day = "21")

                // Assert
                assertAge(25)
            }
        }

    @Test
    fun `should not subtract one for someone born on a leap day when today is a leap day`() =
        runComposeUiTest {
            // Arrange
            showApp(today = LocalDate(2024, 2, 29))

            // Act
            enterDateOfBirth(year = "2000", month = "2", day = "29")

            // Assert
            assertAge(24)
        }

    @Test
    fun `should subtract one for someone born on a leap day on February 28 of a common year`() =
        runComposeUiTest {
            // Arrange
            showApp(today = LocalDate(2026, 2, 28))

            // Act
            enterDateOfBirth(year = "2000", month = "2", day = "29")

            // Assert
            assertAge(25)
        }

    @Test
    fun `should not subtract one for someone born on a leap day on March 1 of a common year`() =
        runComposeUiTest {
            // Arrange
            showApp(today = LocalDate(2026, 3, 1))

            // Act
            enterDateOfBirth(year = "2000", month = "2", day = "29")

            // Assert
            assertAge(26)
        }

    @Test
    fun `should show a negative age for a future leap day in a common year`() =
        runComposeUiTest {
            // Arrange
            showApp(today = LocalDate(2026, 6, 21))

            // Act
            enterDateOfBirth(year = "2028", month = "2", day = "29")

            // Assert
            assertAge(-2)
        }

    @Test
    fun `should show no age for February 29 of a common year when today is in a leap year`() =
        runComposeUiTest {
            // Arrange
            showApp(today = LocalDate(2024, 6, 21))

            // Act
            enterDateOfBirth(year = "2023", month = "2", day = "29")

            // Assert
            assertNoAge()
        }

    private inline fun inBrowserAtNoonUtc(
        minutesBehindUtc: Double,
        block: () -> Unit,
    ) {
        val noonUtc = LocalDate(2026, 6, 20).atTime(12, 0).toInstant(TimeZone.UTC)
        val restoreBrowserDate = replaceBrowserDate(noonUtc.toEpochMilliseconds().toDouble(), minutesBehindUtc)
        try {
            block()
        } finally {
            restoreBrowserDate()
        }
    }

    private suspend fun ComposeUiTest.awaitNodeWithText(text: String): SemanticsNodeInteraction {
        val deadline = TimeSource.Monotonic.markNow() + 1.seconds
        while (onAllNodesWithText(text, useUnmergedTree = true).fetchSemanticsNodes().isEmpty() &&
            deadline.hasNotPassedNow()
        ) {
            withContext(Dispatchers.Default) { delay(10.milliseconds) }
        }
        return onNodeWithText(text, useUnmergedTree = true)
    }

    private suspend fun ComposeUiTest.assertAge(age: Int) {
        awaitNodeWithText("You are $age years old.").assertIsDisplayed()
    }

    private suspend fun ComposeUiTest.assertNoAge() {
        awaitNodeWithText("You are  years old.").assertIsDisplayed()
    }

    private fun ComposeUiTest.showApp(today: LocalDate) {
        showApp(now = today.atStartOfDayIn(TimeZone.UTC), timeZone = TimeZone.UTC)
    }

    private fun ComposeUiTest.showApp(
        now: Instant,
        timeZone: TimeZone,
    ) {
        val fixedClock =
            object : Clock {
                override fun now() = now
            }

        setContent {
            App(clock = fixedClock, timeZone = timeZone)
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
