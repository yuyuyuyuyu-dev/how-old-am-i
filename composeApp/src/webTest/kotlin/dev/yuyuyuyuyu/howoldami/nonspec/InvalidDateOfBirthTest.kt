package dev.yuyuyuyuyu.howoldami.nonspec

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.v2.runComposeUiTest
import dev.yuyuyuyuyu.howoldami.App
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class InvalidDateOfBirthTest {
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
}
