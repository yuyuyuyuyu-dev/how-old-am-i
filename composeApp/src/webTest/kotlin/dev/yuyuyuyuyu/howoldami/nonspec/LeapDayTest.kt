package dev.yuyuyuyuyu.howoldami.nonspec

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.v2.runComposeUiTest
import kotlinx.datetime.LocalDate
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class LeapDayTest {
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
}
