package dev.yuyuyuyuyu.howoldami.nonspec

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.v2.runComposeUiTest
import dev.yuyuyuyuyu.howoldami.App
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.test.Test
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTestApi::class, ExperimentalTime::class)
class AgeCalculationTest {
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
}
